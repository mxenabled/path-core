package com.mx.path.gateway.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Setter;

import com.mx.path.core.common.accessor.AfterAccessorInitialize;
import com.mx.path.core.common.accessor.RootAccessor;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.connect.AccessorConnectionSettings;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.common.reflection.Annotations;
import com.mx.path.core.common.reflection.Fields;
import com.mx.path.core.common.session.ServiceScope;
import com.mx.path.core.utility.reflection.ClassHelper;
import com.mx.path.gateway.GatewayBuilderHelper;
import com.mx.path.gateway.accessor.Accessor;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorConnections;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;
import com.mx.path.gateway.configuration.annotations.ChildAccessors;
import com.mx.path.gateway.configuration.annotations.MaxScope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds and configure accessor stack.
 */
public class AccessorStackConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccessorStackConfigurator.class);

  /**
   * -- SETTER --
   * Set configurator root accessor.
   *
   * @param rootAccessor root accessor to set
   */
  @Setter
  private Accessor rootAccessor;
  private final ConfigurationState state;

  /**
   * Build new {@link AccessorStackConfigurator} instance with provided {@link ConfigurationState}.
   *
   * @param state state to set
   */
  public AccessorStackConfigurator(ConfigurationState state) {
    this.state = state;
  }

  /**
   * Build accessor proxy.
   *
   * @param name name of built accessor
   * @param map configuration data map
   * @param clientId clientId for built accessor
   * @param builder builder object to build accessor
   * @param parent parent of built accessor
   * @return new accessor instance, null if accessor can't be created
   */
  @SuppressWarnings("unchecked")
  public Accessor buildAccessor(String name, ObjectMap map, String clientId, Object builder, Accessor parent) {
    ObjectMap node = map.getMap("accessor");
    Accessor accessor;

    state.pushLevel(name);
    try {
      /**
       * Determine the accessor type and configuration from accessor block OR parent child accessor annotations
       */
      if (node == null) {
        // No accessor node is present
        GatewayBuilderHelper.setRootAccessor(builder, rootAccessor);

        if (parent != null) {
          // Determine if parent has child accessor annotations
          accessor = buildFromParent(name, clientId, parent);
        } else {
          // No parent
          return null;
        }
      } else {
        accessor = buildFromNode(clientId, node);
      }

      if (accessor == null) {
        return null;
      }

      if (Annotations.hasAnnotation(accessor.getClass(), RootAccessor.class)) {
        this.rootAccessor = accessor;
        GatewayBuilderHelper.setRootAccessor(builder, accessor);
      } else {
        GatewayBuilderHelper.setRootAccessor(builder, rootAccessor);
        if (parent != null) {
          Field accessorField = getMatchingAccessorField(accessor.getClass(), parent);
          Fields.setFieldValue(accessorField, parent, accessor);
        }
      }

      return accessor;
    } finally {
      state.popLevel();
    }
  }

  /**
   * Build an accessor given an accessor configuration node.
   *
   * @param clientId clientId for built accessor
   * @param node configuration map for built accessor
   * @return new {@link Accessor} instance
   */
  @SuppressWarnings("unchecked")
  public final Accessor buildFromNode(String clientId, ObjectMap node) {
    AccessorConfiguration.AccessorConfigurationBuilder configurationBuilder = AccessorConfiguration.builder().clientId(clientId);

    // An accessor node is present

    if (node.getMap("configurations") != null) {
      configurationBuilder.configurations(node.getMap("configurations"));
    }

    if (node.getMap("connections") != null) {
      buildConnections(node.getMap("connections"), configurationBuilder);
    }

    Class<? extends Accessor> accessorType = (Class<? extends Accessor>) new ClassHelper().getClass(node.getAs(String.class, "class"));
    validateServiceScope(accessorType);
    AccessorScope accessorScope = determineAccessorScope(node, accessorType);
    Class<?> proxyType = AccessorProxyMap.get(accessorScope.getName(), Accessor.getAccessorBase(accessorType));

    invokeAfterInitializeMethods(accessorType, clientId);

    return new ClassHelper().buildInstance(Accessor.class, proxyType, configurationBuilder.build(), accessorType);
  }

  /**
   * Build an accessor from a parent's child accessor annotations.
   *
   * @param name Name of accessor
   * @param clientId Client ID
   * @param parent Parent Accessor
   * @return new Accessor
   */
  @SuppressWarnings("unchecked")
  public final Accessor buildFromParent(String name, String clientId, Accessor parent) {
    AccessorConfiguration.AccessorConfigurationBuilder configuration = AccessorConfiguration.builder().clientId(clientId);
    AtomicReference<Class<? extends Accessor>> accessorType = new AtomicReference<>();
    AtomicReference<String> accessorScope = new AtomicReference<>();

    try {
      Class<? extends Accessor> parentClass = ((AccessorProxy) parent).getAccessorClass();
      Class<? extends Accessor> parentBaseClass = Accessor.getAccessorBase(parentClass);

      // Determine the base accessor class using the name and fields in the parent's base class
      final Class<? extends Accessor> baseAccessorClass = Accessor.getAccessorBase((Class<? extends Accessor>) parentBaseClass.getDeclaredField(name).getType());
      accessorScope.set(((AccessorProxy) parent).getScope());

      List<ChildAccessor> allChildAccessors = getChildAccessors(parentClass);

      // Child accessor annotations exist. Find matching
      accessorType.set(allChildAccessors.stream().map(ChildAccessor::value).filter(childClass -> Accessor.getAccessorBase(childClass) == baseAccessorClass).findFirst().orElse(null));
      if (accessorType.get() == null) {
        // No child accessor was provided by parent annotations
        return null;
      }

      configuration.configurations(parent.getConfiguration().getConfigurations());
      configuration.connections(parent.getConfiguration().getConnections());

    } catch (NoSuchFieldException e) {
      throw new RuntimeException("Mis-configured accessor: " + name, e);
    }

    Accessor accessor;
    Class<?> proxyType = AccessorProxyMap.get(accessorScope.get(), Accessor.getAccessorBase(accessorType.get()));
    accessor = new ClassHelper().buildInstance(Accessor.class, proxyType, configuration.build(), accessorType.get());

    invokeAfterInitializeMethods(accessorType.get(), clientId);

    return accessor;
  }

  private void buildConnections(ObjectMap map, AccessorConfiguration.AccessorConfigurationBuilder builder) {
    AccessorConnections connections = new AccessorConnections();
    map.keySet().forEach(connectionName -> {
      AccessorConnectionSettings connection = ConnectionBinder.buildConnection(map, connectionName);
      connections.addConnection(connectionName, connection);
    });
    builder.connections(connections);
  }

  /**
   * Determine the scope for this accessor
   *
   * <p>This is determined in the following order (highest precedence on top):
   * <ol>
   *   <li>configuration's 'scope' key</li>
   *   <li>class MaxScope annotation</li>
   * </ol>
   *
   * @param node
   * @param accessorType
   * @return
   */
  private AccessorScope determineAccessorScope(ObjectMap node, Class<? extends Accessor> accessorType) {
    return state.withField("scope", () -> {
      AccessorScope configurationScope = getConfigurationAccessorScope(node);
      MaxScope maxScope = accessorType.getAnnotation(MaxScope.class);
      if (maxScope == null) {
        LOGGER.warn("Class missing MaxScope annotation. This will be required in the future. (" + accessorType.getCanonicalName() + ")");
      }

      AccessorScope calculatedScope;
      if (configurationScope != null) {
        calculatedScope = configurationScope;
      } else if (maxScope != null) {
        calculatedScope = maxScope.value();
      } else {
        throw new ConfigurationError("No scope provided for accessor. (" + accessorType.getCanonicalName() + ")", state);
      }

      if (maxScope != null && calculatedScope.getValue() > maxScope.value().getValue()) {
        throw new ConfigurationError("Configured scope (" + calculatedScope.getName() + ") is higher that specified MaxScope (" + maxScope.value().getName() + ")", state);
      }

      return calculatedScope;
    });
  }

  private List<ChildAccessor> getChildAccessors(Class<? extends Accessor> parentClass) {
    ChildAccessors childAccessors = parentClass.getAnnotation(ChildAccessors.class);
    ChildAccessor childAccessor = parentClass.getAnnotation(ChildAccessor.class);

    List<ChildAccessor> allChildAccessors = new ArrayList<>();
    if (childAccessors != null) {
      Collections.addAll(allChildAccessors, childAccessors.value());
    }
    if (childAccessor != null) {
      allChildAccessors.add(childAccessor);
    }
    return allChildAccessors;
  }

  private AccessorScope getConfigurationAccessorScope(ObjectMap node) {
    String configurationScopeValue = node.getAs(String.class, "scope");
    AccessorScope configurationScope = AccessorScope.resolve(configurationScopeValue);

    if (Strings.isNotBlank(configurationScopeValue) && configurationScope == null) {
      throw new ConfigurationError("Invalid scope (" + configurationScopeValue + ")", state);
    }
    return configurationScope;
  }

  private Field getMatchingAccessorField(Class<? extends Accessor> accessorType, Accessor parent) {
    Field accessorField = Arrays.stream(Accessor.getAccessorBase(parent.getClass()).getDeclaredFields()).filter(field -> field.getType() == Accessor.getAccessorBase(accessorType)).findFirst().orElse(null);
    if (accessorField == null) {
      throw new RuntimeException("Mis-configured accessor stack. Chain is broken");
    }
    return accessorField;
  }

  private void validateServiceScope(Class<? extends Accessor> accessor) {
    if (!accessor.isAnnotationPresent(ServiceScope.class)) {
      LOGGER.warn("Class missing ServiceScope annotation. This will be required in the future. (" + accessor.getCanonicalName() + ")");
    }
  }

  private void invokeAfterInitializeMethods(Class<?> accessorClass, String clientId) {
    Method[] methods = accessorClass.getMethods();

    for (Method method : methods) {
      if (method.isAnnotationPresent(AfterAccessorInitialize.class) && (method.getModifiers() & Modifier.STATIC) != 0) {
        try {
          if (method.getParameterCount() == 0) {
            method.invoke(null);
          } else if (method.getParameterCount() == 1) {
            method.invoke(null, clientId);
          }
        } catch (Exception e) {
          throw new RuntimeException("Failed to invoke @AfterAccessorInitialize method: " + method.getName(), e);
        }
      }
    }
  }
}
