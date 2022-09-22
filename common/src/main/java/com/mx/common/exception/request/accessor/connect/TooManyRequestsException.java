package com.mx.common.exception.request.accessor.connect;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.exception.request.PathRequestException;

/**
 * Thrown in situation where too many concurrent requests are being sent to a service. These constraints are usually
 * stipulated by the Client to ensure their systems can handle the traffic being sent. See {@link com.mx.common.process.FaultTolerantExecutor}
 * facility documentation for details.
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class TooManyRequestsException extends ConnectException {
  public TooManyRequestsException(String message, Throwable cause) {
    super(message, cause);
    setReport(false);
    setStatus(PathResponseStatus.TOO_MANY_REQUESTS);
  }
}
