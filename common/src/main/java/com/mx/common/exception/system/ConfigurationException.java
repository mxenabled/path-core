package com.mx.common.exception.system;

/**
 * Thrown on gateway configuration error
 *
 * <p>
 *   See {@link PathSystemException} for usage details
 * </p>
 */
public class ConfigurationException extends PathSystemException {
  public ConfigurationException(String message) {
    super(message);
  }

  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
