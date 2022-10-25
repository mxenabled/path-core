package com.mx.path.gateway.configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lombok.Setter;

import com.mx.common.accessors.Accessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorConnections;
import com.mx.common.accessors.RootAccessor;
import com.mx.common.collections.ObjectMap;
import com.mx.common.connect.AccessorConnectionSettings;
import com.mx.common.gateway.GatewayException;
import com.mx.common.lang.Strings;
import com.mx.common.reflection.Annotations;
import com.mx.common.reflection.Fields;
import com.mx.common.session.ServiceScope;
import com.mx.path.gateway.GatewayBuilderHelper;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;
import com.mx.path.gateway.configuration.annotations.ChildAccessors;
import com.mx.path.gateway.configuration.annotations.MaxScope;
import com.mx.path.gateway.connect.filters.CallbacksFilter;
import com.mx.path.gateway.connect.filters.ErrorHandlerFilter;
import com.mx.path.gateway.connect.filters.FaultTolerantRequestFilter;
import com.mx.path.gateway.connect.filters.RequestFinishedFilter;
import com.mx.path.gateway.connect.filters.TracingFilter;
import com.mx.path.gateway.connect.filters.UpstreamRequestEventFilter;
import com.mx.path.utilities.reflection.ClassHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessorStackConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccessorStackConfigurator.class);

  @Setter
  private Accessor rootAccessor;
  private final ConfigurationState state;

  public AccessorStackConfigurator(ConfigurationState state) {
    this.state = state;
  }

  /**
   * Build accessor proxy
   *
   * @param name
   * @param map
   * @param clientId
   * @param builder
   * @param parent
   * @return
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
   * @param clientId
   * @param node
   * @return new Accessor
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

    return new ClassHelper().buildInstance(Accessor.class, proxyType, configurationBuilder.build(), accessorType);
  }

  /**
   * Build an accessor from a parent's child accessor annotations
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

    return accessor;
  }

  private void buildConnections(ObjectMap map, AccessorConfiguration.AccessorConfigurationBuilder builder) {
    AccessorConnections connections = new AccessorConnections();
    map.keySet().forEach(connectionName -> {
      AccessorConnectionSettings.AccessorConnectionSettingsBuilder connection = AccessorConnectionSettings.builder();
      connection.baseUrl(map.getMap(connectionName).getAsString("baseUrl"));
      connection.certificateAlias(map.getMap(connectionName).getAsString("certificateAlias"));
      connection.keystorePath(map.getMap(connectionName).getAsString("keystorePath"));
      String passwordString = map.getMap(connectionName).getAsString("keystorePassword");
      if (passwordString != null) {
        connection.keystorePassword(passwordString.toCharArray());
      }
      connection.skipHostNameVerify(Boolean.parseBoolean(String.valueOf(map.getMap(connectionName).get("skipHostNameVerify"))));

      // Default request filters
      // todo: Provide way to configure the request filters in connection block
      connection.baseRequestFilter(new TracingFilter());
      connection.baseRequestFilter(new UpstreamRequestEventFilter());
      connection.baseRequestFilter(new ErrorHandlerFilter());
      connection.baseRequestFilter(new CallbacksFilter());
      connection.baseRequestFilter(new RequestFinishedFilter());
      connection.baseRequestFilter(new FaultTolerantRequestFilter());

      AccessorConnectionSettings conn = connection.build();
      validateConnection(conn);
      connections.addConnection(connectionName, conn);
      if (map.getMap(connectionName).getMap("configurations") != null) {
        map.getMap(connectionName).getMap("configurations").forEach(connection::configuration);
      }
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

  private void validateConnection(AccessorConnectionSettings conn) {
    String keystorePassword = "";
    if (conn.getKeystorePassword() != null) {
      keystorePassword = new String(conn.getKeystorePassword());
    }

    if (Strings.isNotBlank(conn.getCertificateAlias()) || Strings.isNotBlank(conn.getKeystorePath()) || Strings.isNotBlank(keystorePassword)) {
      Map<String, String> values = new LinkedHashMap<>();
      values.put("certificateAlias", conn.getCertificateAlias());
      values.put("keystorePath", conn.getKeystorePath());
      values.put("keystorePassword", keystorePassword);

      List<String> missingKeys = values.entrySet().stream().filter((e) -> Strings.isBlank(e.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());

      if (!missingKeys.isEmpty()) {
        throw new GatewayException("Invalid connection details. Missing " + String.join(", ", missingKeys));
      }
    }
  }

  private void validateServiceScope(Class<? extends Accessor> accessor) {
    if (!accessor.isAnnotationPresent(ServiceScope.class)) {
      LOGGER.warn("Class missing ServiceScope annotation. This will be required in the future. (" + accessor.getCanonicalName() + ")");
    }
  }
}
