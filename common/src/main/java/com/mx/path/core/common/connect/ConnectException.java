package com.mx.path.core.common.connect;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown on error in connection code.
 *
 * <p>Note: Connect exceptions are intended to represent an unrecoverable connection failure. These types of exceptions
 * are not blocked by the Request filters. They are expected to bubble up to top-level exception handling.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class ConnectException extends PathRequestException {
  private static final long serialVersionUID = 1L;

  /**
   * Build new instance of {@link ConnectException} with specified parameters.
   *
   * @param message description message
   * @param cause cause
   */
  public ConnectException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Build new instance of {@link ConnectException} with specified parameters.
   *
   * @param message description message
   * @param status response status
   */
  public ConnectException(String message, PathResponseStatus status) {
    super(message);
    setStatus(status);
    setReport(false);
  }

  /**
   * Build new instance of {@link ConnectException} with specified parameters.
   *
   * @param message description message
   * @param status response status
   * @param cause cause
   */
  public ConnectException(String message, PathResponseStatus status, Throwable cause) {
    super(message, cause);
    setStatus(status);
    setReport(false);
  }
}
