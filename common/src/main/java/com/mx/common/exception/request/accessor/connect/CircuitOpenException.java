package com.mx.common.exception.request.accessor.connect;

import com.mx.common.exception.request.PathRequestException;

/**
 * Thrown on open circuit condition
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class CircuitOpenException extends ServiceUnavailableException {
  public CircuitOpenException(String message, Throwable cause) {
    super(message, cause);
  }
}
