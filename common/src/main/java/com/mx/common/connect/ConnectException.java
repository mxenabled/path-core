package com.mx.common.connect;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.exception.PathRequestException;

/**
 * Thrown on error in connection code
 *
 * <p>
 *   Note: Connect exceptions are intended to represent an unrecoverable connection failure. These types of exceptions
 *   are not blocked by the Request filters. They are expected to bubble up to top-level exception handling.
 * </p>
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class ConnectException extends PathRequestException {
  private static final long serialVersionUID = 1L;

  public ConnectException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConnectException(String message, PathResponseStatus status) {
    super(message);
    setStatus(status);
    setReport(false);
  }

  public ConnectException(String message, PathResponseStatus status, Throwable cause) {
    super(message, cause);
    setStatus(status);
    setReport(false);
  }

  @Deprecated
  public ConnectException(String message, PathResponseStatus status, boolean reportError, Throwable cause) {
    super(message, cause);
    setStatus(status);
    setReport(reportError);
  }
}
