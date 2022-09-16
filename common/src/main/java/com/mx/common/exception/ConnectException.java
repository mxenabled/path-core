package com.mx.common.exception;

import com.mx.common.accessors.PathResponseStatus;

/**
 * Thrown on error in connection code
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class ConnectException extends AccessorException {
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
