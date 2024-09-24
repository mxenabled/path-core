package com.mx.path.core.common.configuration;

import com.mx.path.core.common.exception.PathSystemException;

/**
 * Thrown when invalid code state is detected, at boot time.
 *
 * <p>See {@link PathSystemException} for usage details
 */
public class InvalidStateException extends PathSystemException {

  /**
   * Build {@link InvalidStateException} with specified parameters.
   *
   * @param message message
   */
  public InvalidStateException(String message) {
    super(message);
  }

  /**
   * Build {@link InvalidStateException} with specified parameters.
   *
   * @param cause cause
   */
  public InvalidStateException(Throwable cause) {
    super(cause);
  }

  /**
   * Build {@link InvalidStateException} with specified parameters.
   *
   * @param message message
   * @param cause cause
   */
  public InvalidStateException(String message, Throwable cause) {
    super(message, cause);
  }
}
