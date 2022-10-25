package com.mx.path.gateway.configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mx.common.collections.ObjectMap;
import com.mx.common.configuration.Configuration;
import com.mx.common.configuration.ConfigurationField;
import com.mx.path.gateway.configuration.annotations.ClientID;
import com.mx.path.utilities.reflection.ClassHelper;

/**
 * General purpose object configurator. Given an ObjectMap of configurations, this class will instantiate an object with
 * (optional) configuration binding using the {@link Configuration}
 * and {@link ConfigurationField} annotations.
 *
 * Example:
 *
 * <pre>
 *    {@code
 *       ObjectMap node = new ObjectMap();
 *       node.put("class", "com.mx.MyGatewayBehavior");
 *
 *       ObjectMap configurations = new ObjectMap();
 *       configurations.put("active", false);
 *       configurations.put("retryMillis", 1000L);
 *
 *       node.put("configurations", configurations);
 *
 *       MyGatewayBehavior myGatewayBehavior = new GatewayObjectConfigurator(state).buildFromNode(node, "clientId", MyGatewayBehavior.class);
 *
 *       myGatewayBehavior.getConfiguration().isActive(); // true
 *       myGatewayBehavior.getConfiguration().getRetryMillis(); // 1000L
 *
 *       // Using the superclass if you don't know the concrete type at runtime.
 *
 *       GatewayBehavior behavior = new GatewayObjectConfigurator(state).buildFromNode(node, "clientId", GatewayBehavior.class);
 *    }
 * </pre>
 */
public final class GatewayObjectConfigurator {
  private final ConfigurationState state;

  public GatewayObjectConfigurator(ConfigurationState state) {
    this.state = state;
  }

  public <T> T buildFromNode(ObjectMap map, String clientId, Class<T> klass) {
    ConfigurationBinder binder = new ConfigurationBinder(clientId, state);

    Class<?> targetClass = new ClassHelper().getClass(map.getAsString("class"));
    ObjectMap configurations = map.getMap("configurations");

    if (!klass.isAssignableFrom(targetClass)) {
      throw new ConfigurationError(klass.getCanonicalName() + " is not assignable from " + targetClass.getCanonicalName(), state);
    }

    return state.withLevel(targetClass.getSimpleName(), () -> {
      Constructor<?> constructor = findBestConstructor(targetClass);

      List<Object> constructorArgs = new ArrayList<>();

      Arrays.stream(constructor.getParameters()).forEach(param -> {
        if (param.getType() == ObjectMap.class) {
          constructorArgs.add(configurations);
          return;
        }

        if (param.isAnnotationPresent(ClientID.class)) {
          constructorArgs.add(clientId);
          return;
        }

        Configuration configurationAnnotation = param.getAnnotation(Configuration.class);
        if (configurationAnnotation != null) {
          state.withLevel(param.getType().getSimpleName(), () -> {
            constructorArgs.add(binder.build(param.getType(), configurations));
          });
        }
      });

      return (T) build(constructor, constructorArgs);
    });
  }

  private <T> T build(Constructor<T> constructor, List<Object> args) {
    try {
      return constructor.newInstance(args.toArray());
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new ConfigurationError("Unable to construct object", state);
    }
  }

  private <T> Constructor<T> findBestConstructor(Class<T> klass) {
    List<Constructor<?>> constructors = Arrays.asList(klass.getDeclaredConstructors());

    constructors = constructors.stream()
        .filter(constructor -> Arrays.stream(constructor.getParameters()).allMatch(
            param -> param.getType() == ObjectMap.class
                || param.isAnnotationPresent(Configuration.class)
                || param.isAnnotationPresent(ClientID.class)))
        .collect(Collectors.toList());

    if (constructors.size() > 1) {
      throw new ConfigurationError("Too many valid constructors for " + klass.getCanonicalName(), state);
    }

    if (constructors.isEmpty()) {
      throw new ConfigurationError("No valid constructors for " + klass.getCanonicalName(), state);
    }

    return (Constructor<T>) constructors.get(0);
  }
}
