package com.mx.common.exception.request.accessor.connect;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.exception.request.PathRequestException;

/**
 * Thrown when a service (typically upstream service) is unavailable. The unavailability could be determined by a response
 * from the service or something on the client side.
 *
 * <p>
 *   Use {@link CircuitOpenException} for open circuit situations.
 * </p>
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class ServiceUnavailableException extends ConnectException {
  public ServiceUnavailableException(String message, Throwable cause) {
    super(message, cause);
    setReport(false);
    setStatus(PathResponseStatus.UNAVAILABLE);
  }
}
