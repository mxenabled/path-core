package com.mx.path.core.common.exception;

import com.mx.path.core.common.configuration.ConfigurationException;
import com.mx.path.core.common.configuration.InvalidStateException;

/**
 * Base exception for any system-based error. These errors occur on incorrect application state or configuration,
 * typically at application boot time.
 *
 * <p>Hierarchy:
 *
 * <pre>
 *   {@link ConfigurationException}
 *   {@link InvalidStateException}
 * </pre>
 */
public abstract class PathSystemException extends RuntimeException {

  /**
   * Build new {@link PathSystemException} with specified parameters.
   *
   * @param message message
   */
  public PathSystemException(String message) {
    super(message == null ? "" : message);
  }

  /**
   * Build new {@link PathSystemException} with specified parameters.
   *
   * @param cause cause
   */
  public PathSystemException(Throwable cause) {
    super(cause);
  }

  /**
   * Build new {@link PathSystemException} with specified parameters.
   *
   * @param message message
   * @param cause cause
   */
  public PathSystemException(String message, Throwable cause) {
    super(message == null ? "" : message, cause);
  }
}
