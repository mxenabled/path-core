package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when a request is malformed or missing required data
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class BadRequestException extends AccessorUserException {
  public BadRequestException(String message) {
    super(message, PathResponseStatus.BAD_REQUEST);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, PathResponseStatus.BAD_REQUEST, cause);
  }

  @Deprecated
  public BadRequestException(String message, String userMessage) {
    super(message, userMessage, PathResponseStatus.BAD_REQUEST);
  }

  @Deprecated
  public BadRequestException(String message, String userMessage, Throwable cause) {
    super(message, userMessage, PathResponseStatus.BAD_REQUEST, cause);
  }
}
