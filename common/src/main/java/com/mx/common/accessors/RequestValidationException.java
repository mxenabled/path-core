package com.mx.common.accessors;

import com.mx.common.exception.PathRequestException;

/**
 * Thrown when a request is correctly formed, but the data is invalid for some reason
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class RequestValidationException extends AccessorUserException {
  public RequestValidationException(String message, String userMessage) {
    super(message, userMessage, PathResponseStatus.USER_ERROR);
  }

  public RequestValidationException(String message, String userMessage, Throwable cause) {
    super(message, userMessage, PathResponseStatus.USER_ERROR, cause);
  }
}