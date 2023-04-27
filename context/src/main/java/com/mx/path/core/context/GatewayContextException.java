package com.mx.path.core.context;

public class GatewayContextException extends RuntimeException {
  public GatewayContextException(String message) {
    super(message);
  }

  public GatewayContextException(String message, Throwable cause) {
    super(message, cause);
  }
}
