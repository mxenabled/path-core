package com.mx.path.gateway.configuration;

import com.mx.path.core.common.lang.Strings;

/**
 * Raise when a configuration error is encountered. Works with {@link ConfigurationState} to report the location
 * of the error.
 */
public class ConfigurationError extends RuntimeException {

  /**
   * Build new {@link ConfigurationError} with specified parameters.
   *
   * @param message message
   * @param field field
   */
  public ConfigurationError(String message, String field) {
    super(buildMessage(message, field, null));
  }

  /**
   * Build new {@link ConfigurationError} with specified parameters.
   *
   * @param message message
   * @param field field
   * @param cause cause
   */
  public ConfigurationError(String message, String field, Throwable cause) {
    super(buildMessage(message, field, null), cause);
  }

  /**
   * Build new {@link ConfigurationError} with specified parameters.
   *
   * @param message message
   * @param state state
   */
  public ConfigurationError(String message, ConfigurationState state) {
    this(message, state, null);
  }

  /**
   * Build new {@link ConfigurationError} with specified parameters.
   *
   * @param message message
   * @param state state
   * @param cause cause
   */
  public ConfigurationError(String message, ConfigurationState state, Throwable cause) {
    super(buildMessage(message, null, state), cause);
  }

  /**
   * Build description message for this exception.
   *
   * @param message message
   * @param field field
   * @param state state
   * @return description message
   */
  private static String buildMessage(String message, String field, ConfigurationState state) {
    StringBuilder builder = new StringBuilder();
    builder.append(message);

    if (Strings.isBlank(field) && state != null) {
      field = state.field();
    }

    if (Strings.isNotBlank(field)) {
      builder.append(" on ");
      builder.append(field);
    }

    if (state != null) {
      builder.append(" at ");
      builder.append(state.currentState());
    }

    return builder.toString();
  }

}
