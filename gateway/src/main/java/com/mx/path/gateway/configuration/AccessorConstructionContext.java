package com.mx.path.gateway.configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.core.common.collection.ObjectArray;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.core.common.connect.AccessorConnectionSettings;
import com.mx.path.core.common.gateway.GatewayException;
import com.mx.path.core.common.serialization.ObjectMapJsonDeserializer;
import com.mx.path.gateway.accessor.Accessor;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.ClientID;
import com.mx.path.gateway.configuration.annotations.Connection;

/**
 * Holds information needed to construct an accessor from configuration. Use the {@link #build()} to construct a new instance of {@link #accessorClass}.
 * @param <T> The Accessor Base class for this instance.
 */
public class AccessorConstructionContext<T extends Accessor> {

  @Getter
  private final List<Object> constructorArgs = new ArrayList<>();
  @Getter
  private final Map<String, ConnectionConstructionContext> connections = new HashMap<>();
  @Getter
  private final Class<? extends T> accessorClass;
  @Getter
  private final AccessorConfiguration accessorConfiguration;
  @Getter
  private final ConfigurationState state;
  @Getter
  private Constructor<? extends T> constructor;

  public <V extends T> AccessorConstructionContext(Class<V> accessorClass, AccessorConfiguration configuration) {
    this.accessorConfiguration = configuration;
    this.accessorClass = accessorClass;
    this.state = ConfigurationState.getCurrent();
    ConfigurationBinder binder = new ConfigurationBinder(configuration.getClientId(), state);
    state.withLevel("accessor", () -> {
      state.withLevel(accessorClass.getSimpleName(), () -> {

        // discover and save constructor
        constructor = findBestConstructor(accessorClass);

        // build, bind, and validate configuration objects
        // setup constructor params array
        Arrays.stream(constructor.getParameters()).forEach(param -> {
          if (AccessorConfiguration.class.isAssignableFrom(param.getType())) {
            constructorArgs.add(configuration);
            return;
          }

          if (param.isAnnotationPresent(ClientID.class)) {
            constructorArgs.add(accessorConfiguration.getClientId());
            return;
          }

          Configuration configurationAnnotation = param.getAnnotation(Configuration.class);
          if (configurationAnnotation != null) {
            state.withLevel(param.getType().getSimpleName(), () -> {
              constructorArgs.add(binder.build(param.getType(), configuration.getConfigurations()));
            });
            return;
          }

          Connection connectionAnnotation = param.getAnnotation(Connection.class);
          if (connectionAnnotation != null) {

            state.withLevel("connections." + connectionAnnotation.value(), () -> {

              AccessorConnectionSettings accessorConnectionSettings = getAccessorConfiguration().getConnections().getConnection(connectionAnnotation.value());
              if (accessorConnectionSettings == null) {
                throw new ConfigurationError("No connection configuration provided for " + connectionAnnotation.value(), state);
              }
              ConnectionConstructionContext connectionConstructionContext = new ConnectionConstructionContext(accessorConfiguration.getClientId(), state, param.getType(), accessorConnectionSettings);
              constructorArgs.add(connectionConstructionContext);
              connections.put(connectionAnnotation.value(), connectionConstructionContext);

            });

            return;
          }

          throw new ConfigurationError("Unable to bind param " + param.getName() + " for constructor " + accessorClass.getCanonicalName(), state);
        });

        // Do a test build!
        build();

      });
    });
  }

  public final T build() {
    try {
      return getConstructor().newInstance(buildConstructorArgs().toArray());
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new GatewayException("Unable to construct accessor " + getAccessorClass().getCanonicalName(), e);
    }
  }

  public final void describe(ObjectMap description) {
    GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(ObjectMap.class, new ObjectMapJsonDeserializer());
    Gson gson = gsonBuilder.create();

    getAccessorConfiguration().describe(description);

    List<Object> configurationArguments = constructorArgs.stream()
        .filter((arg) -> !AccessorConfiguration.class.isAssignableFrom(arg.getClass()) && !AccessorConnectionSettings.class.isAssignableFrom(arg.getClass()))
        .collect(Collectors.toList());

    if (!configurationArguments.isEmpty()) {
      // Inject configuration bindings
      ObjectArray boundConfigurations = description.getMap("configurations").createArray(":bindings:");
      configurationArguments.forEach(arg -> {
        if (arg.getClass() != ConnectionConstructionContext.class) {
          ObjectMap argumentDescription = boundConfigurations.createMap();
          argumentDescription.put("class", arg.getClass().getCanonicalName());
          argumentDescription.put(arg.getClass().getSimpleName(), gson.fromJson(gson.toJson(arg), ObjectMap.class));
        }
      });
    }

    if (!connections.isEmpty()) {
      // Inject connection bindings
      ObjectMap boundConfigurations = description.getMap("connections").createMap(":bindings:");
      connections.forEach((name, connectionConstructionContext) -> {
        Object connection = connectionConstructionContext.build();
        ObjectMap connectionDescription = boundConfigurations.createMap(name);
        ObjectMap map = gson.fromJson(gson.toJson(connection), ObjectMap.class);
        connectionDescription.put("class", connection.getClass().getCanonicalName());
        connectionDescription.put(connection.getClass().getSimpleName(), map);
      });
    }
  }

  /**
   * @return args, ready for use in accessor constructor
   */
  private List<Object> buildConstructorArgs() {
    return constructorArgs.stream().map(arg -> {
      if (ConnectionConstructionContext.class.isAssignableFrom(arg.getClass())) {
        return ((ConnectionConstructionContext) arg).build();
      }
      return arg;
    }).collect(Collectors.toList());
  }

  /**
   * Find the best constructor for given Accessor class
   * <p> Constructor must accept one AccessorConfiguration. It can optionally accept a parameter annotated @Configuration
   * and a parameter annotated @Connection.
   * @param klass Accessor Class
   * @param <K> Accessor Class (Extends {@link T})
   * @return {@link Constructor<T>}
   */
  private <K extends T> Constructor<K> findBestConstructor(Class<K> klass) {
    List<Constructor<?>> constructors = Arrays.asList(klass.getDeclaredConstructors());

    // Valid constructors take an AccessorConfiguration
    constructors = constructors.stream().filter(c -> Arrays.stream(c.getParameters()).anyMatch(param -> param.getType() == AccessorConfiguration.class)).collect(Collectors.toList());

    if (constructors.isEmpty()) {
      throw new ConfigurationError("Accessor Constructor must accept AccessorConfiguration: " + klass.getCanonicalName(), state);
    }

    // Find a constructor that has only AccessorConfiguration or AccessorConfiguration with Configuration and Connection annotated params
    constructors = constructors.stream().filter(
        c -> Arrays.stream(c.getParameters()).allMatch(
            param -> param.getType() == AccessorConfiguration.class
                || param.isAnnotationPresent(Configuration.class)
                || param.isAnnotationPresent(Connection.class)
                || param.isAnnotationPresent(ClientID.class)))
        .collect(Collectors.toList());

    if (constructors.size() > 1) {
      throw new ConfigurationError("Too many valid constructors for " + klass.getCanonicalName(), state);
    }

    if (constructors.isEmpty()) {
      throw new ConfigurationError("No valid constructors for " + klass.getCanonicalName(), state);
    }

    // There should only be one constructor left
    return (Constructor<K>) constructors.get(0);
  }

}
