package com.mx.path.gateway.configuration;

import com.mx.path.core.common.configuration.ConfigurationException;

/**
 * Interface for Configurations and GatewayObjects (Facilities, GatewayBehaviors, GatewayServices)
 *
 * <p>Event Order: {@link #initialize()}, {@link #validate(ConfigurationState)}
 */
public interface Configurable {
  /**
   * Optionally, add to class and implement to have the instance initialize itself after data binding is complete
   */
  default void initialize() {
  }

  /**
   * Optionally, add to class and implement custom validation.
   * Throw {@link ConfigurationException} on validation failure
   */
  default void validate(ConfigurationState state) {
  }
}
