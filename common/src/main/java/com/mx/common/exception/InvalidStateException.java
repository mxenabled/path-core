package com.mx.common.exception;

/**
 * Thrown when invalid code state is detected, at boot time.
 *
 * <p>
 *   See {@link PathSystemException} for usage details
 * </p>
 */
public class InvalidStateException extends PathSystemException {
  public InvalidStateException(String message) {
    super(message);
  }

  public InvalidStateException(Throwable cause) {
    super(cause);
  }

  public InvalidStateException(String message, Throwable cause) {
    super(message, cause);
  }
}
