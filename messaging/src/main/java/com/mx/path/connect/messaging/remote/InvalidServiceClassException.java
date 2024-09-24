package com.mx.path.connect.messaging.remote;

/**
 * Exception thrown to indicate that a service class is invalid or not usable.
 */
public class InvalidServiceClassException extends RuntimeException {

  /**
   * Build new {@link InvalidServiceClassException} with specified message.
   *
   * @param message message to be reported with the exception
   */
  public InvalidServiceClassException(String message) {
    super(message);
  }

  /**
   * Build new {@link InvalidServiceClassException} with specified message and cause
   *
   * @param message message to be reported with the exception
   * @param e cause of the exception (a {@link Throwable} that caused this exception to be thrown)
   */
  public InvalidServiceClassException(String message, Throwable e) {
    super(message, e);
  }
}
