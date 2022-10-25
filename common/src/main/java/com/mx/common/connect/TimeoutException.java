package com.mx.common.connect;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.exception.PathRequestException;

/**
 * Thrown on a connection/request timeout
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class TimeoutException extends ConnectException {
  public TimeoutException() {
    super("A request timeout occurred", PathResponseStatus.TIMEOUT);
    setReport(false);
  }

  public TimeoutException(String message) {
    super(message, PathResponseStatus.TIMEOUT);
    setReport(false);
  }

  public TimeoutException(String message, Throwable cause) {
    super(message, PathResponseStatus.TIMEOUT, cause);
    setReport(false);
  }
}
