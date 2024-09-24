package com.mx.path.core.common.connect;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown on a connection/request timeout.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class TimeoutException extends ConnectException {

  /**
   * Default constructor.
   */
  public TimeoutException() {
    super("A request timeout occurred", PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
    setCode(String.valueOf(PathResponseStatus.TIMEOUT.value()));
    setReport(false);
  }

  /**
   * Build new {@link TimeoutException} with specified message.
   *
   * @param message message
   */
  public TimeoutException(String message) {
    super(message, PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
    setCode(String.valueOf(PathResponseStatus.TIMEOUT.value()));
    setReport(false);
  }

  /**
   * Build new {@link TimeoutException} with specified message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public TimeoutException(String message, Throwable cause) {
    super(message, PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE, cause);
    setCode(String.valueOf(PathResponseStatus.TIMEOUT.value()));
    setReport(false);
  }
}
