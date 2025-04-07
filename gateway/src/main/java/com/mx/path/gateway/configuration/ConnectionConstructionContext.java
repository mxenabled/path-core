package com.mx.path.gateway.configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.core.common.connect.AccessorConnectionSettings;
import com.mx.path.core.common.gateway.GatewayException;
import com.mx.path.core.common.reflection.Constructors;
import com.mx.path.core.common.reflection.Fields;
import com.mx.path.gateway.configuration.annotations.Connection;

/**
 * Context for a connection construction.
 */
public class ConnectionConstructionContext {

  /**
   * -- GETTER --
   * Return connection settings.
   *
   * @return connection settings
   */
  @Getter
  private final AccessorConnectionSettings accessorConnectionSettings;

  /**
   * -- GETTER --
   * Return client id.
   *
   * @return client id
   */
  @Getter
  private final String clientId;

  /**
   * -- GETTER --
   * Return connection settings class.
   *
   * @return connection settings class
   */
  @Getter
  private final Class<? extends AccessorConnectionSettings> connectionClass;

  /**
   * -- GETTER --
   * Return connection constructor.
   *
   * @return connection constructor
   */
  @Getter
  private final Constructor<? extends AccessorConnectionSettings> connectionConstructor;

  /**
   * -- GETTER --
   * Return constructor arguments.
   *
   * @return constructor arguments
   */
  @Getter
  private final List<Object> constructorArgs;

  /**
   * -- GETTER --
   * Return configuration state.
   *
   * @return configuration state
   */
  @Getter
  private final ConfigurationState state;

  /**
   * Build new instance of {@link ConnectionConstructionContext}.
   *
   * @param clientId client id
   * @param state state
   * @param connectionClass connection class
   * @param accessorConnectionSettings connection settings accessor
   */
  @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
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
              if (accessorConnectionSettings != null) {
                return configurationBinder.build(parameter.getType(), accessorConnectionSettings.getConfigurations());
              } else {
                return null;
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

  /**
   * Build settings.
   *
   * @return settings
   */
  public final AccessorConnectionSettings build() {
    try {
      if (accessorConnectionSettings != null) {
        AccessorConnectionSettings newAccessorConnectionSettings = getConnectionConstructor().newInstance(getConstructorArgs().toArray());

        // Clone the fields
        Arrays.stream(AccessorConnectionSettings.class.getDeclaredFields()).forEach((field) -> {
          state.withField(field.getName(), () -> Fields.setFieldValue(field, newAccessorConnectionSettings, Fields.getFieldValue(field, accessorConnectionSettings)));
        });

        return newAccessorConnectionSettings;
      } else {
        return null;
      }
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new GatewayException("Unable to construct connection " + getConnectionClass().getCanonicalName(), e);
    }
  }

  /**
   * Find correct constructor to build connection settings.
   *
   * @param klass klass of connection settings
   * @return constructor
   * @param <T> type of connection
   */
  private <T extends AccessorConnectionSettings> Constructor<T> findBestConstructor(Class<T> klass) {
    List<Constructor<T>> constructors = getConstructors(klass);

    // Find a constructor that has only ClientID, Configuration and Connection annotated params
    constructors = constructors.stream().filter(
        c -> Arrays.stream(c.getParameters()).allMatch(param -> param.getAnnotation(Configuration.class) != null
            || param.getAnnotation(Connection.class) != null))
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
