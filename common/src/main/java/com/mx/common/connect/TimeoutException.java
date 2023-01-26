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
    super("A request timeout occurred", PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
    setCode(String.valueOf(PathResponseStatus.TIMEOUT.value()));
    setReport(false);
  }

  public TimeoutException(String message) {
    super(message, PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
    setCode(String.valueOf(PathResponseStatus.TIMEOUT.value()));
    setReport(false);
  }

  public TimeoutException(String message, Throwable cause) {
    super(message, PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE, cause);
    setCode(String.valueOf(PathResponseStatus.TIMEOUT.value()));
    setReport(false);
  }
}
