package com.mx.common.configuration;

import com.mx.common.exception.PathSystemException;

/**
 * Thrown when invalid code state is detected, at boot time.
 *
 * <p>See {@link PathSystemException} for usage details
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
