package com.mx.path.core.common.serialization;

import com.mx.path.core.common.exception.PathSystemException;

/**
 * To be thrown on serialization problems.
 */
public class SerializationException extends PathSystemException {

  /**
   * Build new {@link SerializationException} with specified message.
   *
   * @param message message
   */
  public SerializationException(String message) {
    super(message);
  }

  /**
   * Build new {@link SerializationException} with specified message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public SerializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
