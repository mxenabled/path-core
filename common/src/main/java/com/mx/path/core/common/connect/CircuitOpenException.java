package com.mx.path.core.common.connect;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown on open circuit condition.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class CircuitOpenException extends ServiceUnavailableException {

  /**
   * Build new {@link CircuitOpenException} with description message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public CircuitOpenException(String message, Throwable cause) {
    super(message, cause);
    setInternal(true);
  }
}
