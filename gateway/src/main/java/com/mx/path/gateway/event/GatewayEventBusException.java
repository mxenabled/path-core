package com.mx.path.gateway.event;

/**
 * Exception for problems on event bus.
 */
public class GatewayEventBusException extends RuntimeException {
  /**
   * Build new {@link GatewayEventBusException} with specified message.
   *
   * @param message message
   */
  public GatewayEventBusException(String message) {
    super(message);
  }
}
