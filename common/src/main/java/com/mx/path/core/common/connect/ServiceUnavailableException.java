package com.mx.path.core.common.connect;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when a service (typically upstream service) is unavailable. The unavailability could be determined by a response
 * from the service or something on the client side.
 *
 * <p>Use {@link CircuitOpenException} for open circuit situations.
 *
 * <p>See {@link PathRequestException} for usage details
 *
 */
public class ServiceUnavailableException extends ConnectException {
  public ServiceUnavailableException(String message, Throwable cause) {
    super(message, cause);
    setCode(String.valueOf(PathResponseStatus.UNAVAILABLE.value()));
    setReport(false);
    setStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
  }
}
