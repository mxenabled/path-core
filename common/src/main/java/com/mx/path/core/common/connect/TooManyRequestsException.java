package com.mx.path.core.common.connect;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;
import com.mx.path.core.common.process.FaultTolerantExecutor;

/**
 * Thrown in situation where too many concurrent requests are being sent to a service. These constraints are usually
 * stipulated by the Client to ensure their systems can handle the traffic being sent. See {@link FaultTolerantExecutor}
 * facility documentation for details.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class TooManyRequestsException extends ConnectException {

  /**
   * Build new {@link TooManyRequestsException} with specified message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public TooManyRequestsException(String message, Throwable cause) {
    super(message, cause);
    setCode(String.valueOf(PathResponseStatus.TOO_MANY_REQUESTS.value()));
    setInternal(true);
    setReport(false);
    setStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
  }
}
