package com.mx.common.accessors;

import com.mx.common.exception.PathRequestException;

/**
 * Thrown when a request is malformed or missing required data
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class BadRequestException extends AccessorUserException {
  public BadRequestException(String message, String userMessage) {
    super(message, userMessage, PathResponseStatus.BAD_REQUEST);
  }

  public BadRequestException(String message, String userMessage, Throwable cause) {
    super(message, userMessage, PathResponseStatus.BAD_REQUEST, cause);
  }
}
