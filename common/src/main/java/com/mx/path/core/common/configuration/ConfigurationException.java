package com.mx.path.core.common.configuration;

import com.mx.path.core.common.exception.PathSystemException;

/**
 * Thrown on gateway configuration error.
 *
 * <p>See {@link PathSystemException} for usage details
 */
public class ConfigurationException extends PathSystemException {

  /**
   * Build new {@link ConfigurationException} with specified message.
   *
   * @param message message
   */
  public ConfigurationException(String message) {
    super(message);
  }

  /**
   * Build new {@link ConfigurationException} with specified message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
