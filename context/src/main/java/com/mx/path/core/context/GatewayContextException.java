package com.mx.path.core.context;

/**
 * Throw on gateway context errors.
 */
public class GatewayContextException extends RuntimeException {

  /**
   * Build new {@link GatewayContextException} with specified message.
   *
   * @param message message
   */
  public GatewayContextException(String message) {
    super(message);
  }

  /**
   * Build new {@link GatewayContextException} with specified message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public GatewayContextException(String message, Throwable cause) {
    super(message, cause);
  }
}
