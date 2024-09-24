package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when a request is correctly formed, but the data is invalid for some reason.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class RequestValidationException extends AccessorUserException {

  /**
   * Build new {@link RequestValidationException} with specified parameters.
   *
   * @param message message
   */
  public RequestValidationException(String message) {
    super(message, PathResponseStatus.USER_ERROR);
  }

  /**
   * Build new {@link RequestValidationException} with specified parameters.
   *
   * @param message message
   * @param cause cause
   */
  public RequestValidationException(String message, Throwable cause) {
    super(message, PathResponseStatus.USER_ERROR, cause);
  }

  /**
   * Build new {@link RequestValidationException} with specified parameters.
   *
   * @param message message
   * @param userMessage user message
   */
  public RequestValidationException(String message, String userMessage) {
    super(message, userMessage, PathResponseStatus.USER_ERROR);
  }

  /**
   * Build new {@link RequestValidationException} with specified parameters.
   *
   * @param message message
   * @param userMessage user message
   * @param cause cause
   */
  public RequestValidationException(String message, String userMessage, Throwable cause) {
    super(message, userMessage, PathResponseStatus.USER_ERROR, cause);
  }
}
