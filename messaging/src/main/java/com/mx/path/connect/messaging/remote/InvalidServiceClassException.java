package com.mx.path.connect.messaging.remote;

public class InvalidServiceClassException extends RuntimeException {
  public InvalidServiceClassException(String message) {
    super(message);
  }

  public InvalidServiceClassException(String message, Throwable e) {
    super(message, e);
  }
}
