package com.mx.common.connect;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.exception.PathRequestException;

/**
 * Thrown in situation where too many concurrent requests are being sent to a service. These constraints are usually
 * stipulated by the Client to ensure their systems can handle the traffic being sent. See {@link com.mx.common.process.FaultTolerantExecutor}
 * facility documentation for details.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class TooManyRequestsException extends ConnectException {
  public TooManyRequestsException(String message, Throwable cause) {
    super(message, cause);
    setCode(String.valueOf(PathResponseStatus.TOO_MANY_REQUESTS.value()));
    setReport(false);
    setStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
  }
}
