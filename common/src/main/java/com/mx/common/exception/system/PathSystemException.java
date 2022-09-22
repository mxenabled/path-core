package com.mx.common.exception.system;

/**
 * Base exception for any system-based error. These errors occur on incorrect application state or configuration,
 * typically at application boot time.
 *
 * <p>
 *   Hierarchy:
 *   <pre>
 *     {@link ConfigurationException}
 *     {@link InvalidStateException}
 *   </pre>
 * </p>
 */
public abstract class PathSystemException extends RuntimeException {
  public PathSystemException(String message) {
    super(message == null ? "" : message);
  }

  public PathSystemException(Throwable cause) {
    super(cause);
  }

  public PathSystemException(String message, Throwable cause) {
    super(message == null ? "" : message, cause);
  }
}
