package com.mx.path.gateway.configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

import com.mx.common.configuration.Configuration;
import com.mx.common.connect.AccessorConnectionSettings;
import com.mx.common.reflection.Constructors;
import com.mx.common.reflection.Fields;
import com.mx.path.gateway.GatewayException;
import com.mx.path.gateway.configuration.annotations.ClientID;
import com.mx.path.gateway.configuration.annotations.Connection;

public class ConnectionConstructionContext {

  @Getter
  private final AccessorConnectionSettings accessorConnectionSettings;

  @Getter
  private final String clientId;

  @Getter
  private final Class<? extends AccessorConnectionSettings> connectionClass;

  @Getter
  private final Constructor<? extends AccessorConnectionSettings> connectionConstructor;

  @Getter
  private final List<Object> constructorArgs;

  @Getter
  private final ConfigurationState state;

  @SuppressWarnings("unchecked")
  public ConnectionConstructionContext(String clientId, ConfigurationState state, Class<?> connectionClass, AccessorConnectionSettings accessorConnectionSettings) {
    this.clientId = clientId;
    this.state = state;

    if (!AccessorConnectionSettings.class.isAssignableFrom(connectionClass)) {
      throw new ConfigurationError("Connection does not implement AccessorConnectionSettings: " + connectionClass.getCanonicalName(), state);
    }

    this.connectionClass = (Class<? extends AccessorConnectionSettings>) connectionClass;
    this.accessorConnectionSettings = accessorConnectionSettings;
    this.connectionConstructor = findBestConstructor(this.connectionClass);

    ConfigurationBinder configurationBinder = new ConfigurationBinder(clientId, state);
    state.pushLevel(connectionClass.getSimpleName());
    try {
      constructorArgs = Arrays.stream(connectionConstructor.getParameters())
          .map(parameter -> {
            state.pushLevel(parameter.getType().getSimpleName());
            try {
              if (parameter.isAnnotationPresent(ClientID.class)) {
                return clientId;
              } else {
                return configurationBinder.build(parameter.getType(), accessorConnectionSettings.getConfigurations());
              }
            } finally {
              state.popLevel();
            }
          })
          .collect(Collectors.toList());

    } finally {
      state.popLevel();
    }
  }

  public final AccessorConnectionSettings build() {
    try {
      AccessorConnectionSettings newAccessorConnectionSettings = getConnectionConstructor().newInstance(getConstructorArgs().toArray());

      // Clone the fields
      Arrays.stream(AccessorConnectionSettings.class.getDeclaredFields()).forEach((field) -> {
        state.withField(field.getName(), () -> Fields.setFieldValue(field, newAccessorConnectionSettings, Fields.getFieldValue(field, accessorConnectionSettings)));
      });

      return newAccessorConnectionSettings;
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new GatewayException("Unable to construct connection " + getConnectionClass().getCanonicalName(), e);
    }
  }

  private <T extends AccessorConnectionSettings> Constructor<T> findBestConstructor(Class<T> klass) {
    List<Constructor<T>> constructors = getConstructors(klass);

    // Find a constructor that has only ClientID, Configuration and Connection annotated params
    constructors = constructors.stream().filter(
        c -> Arrays.stream(c.getParameters()).allMatch(param -> param.getAnnotation(Configuration.class) != null
            || param.getAnnotation(Connection.class) != null
            || param.getAnnotation(ClientID.class) != null))
        .collect(Collectors.toList());

    if (constructors.size() == 1) {
      return constructors.get(0);
    }

    if (constructors.size() > 1) {
      throw new ConfigurationError("Too many valid constructors for " + klass.getCanonicalName(), state);
    }

    Constructor<T> constructor = Constructors.getNoArgumentConstructor(klass);
    if (constructor != null) {
      return constructor;
    }

    throw new ConfigurationError("No valid constructors for " + klass.getCanonicalName(), state);
  }

  @SuppressWarnings("unchecked")
  private <T extends AccessorConnectionSettings> List<Constructor<T>> getConstructors(Class<T> klass) {
    return Arrays.stream(klass.getConstructors())
        .map(constructor -> (Constructor<T>) constructor)
        .collect(Collectors.toList());
  }
}
