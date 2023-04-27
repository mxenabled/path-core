package com.mx.path.core.common.connect;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown on open circuit condition
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class CircuitOpenException extends ServiceUnavailableException {
  public CircuitOpenException(String message, Throwable cause) {
    super(message, cause);
  }
}
