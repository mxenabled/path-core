package com.mx.path.gateway.configuration;

import com.mx.path.core.common.lang.Strings;

/**
 * Raise when a configuration error is encountered. Works with {@link ConfigurationState} to report the location
 * of the error.
 */
public class ConfigurationError extends RuntimeException {

  public ConfigurationError(String message, String field) {
    super(buildMessage(message, field, null));
  }

  public ConfigurationError(String message, String field, Throwable cause) {
    super(buildMessage(message, field, null), cause);
  }

  public ConfigurationError(String message, ConfigurationState state) {
    this(message, state, null);
  }

  public ConfigurationError(String message, ConfigurationState state, Throwable cause) {
    super(buildMessage(message, null, state), cause);
  }

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
