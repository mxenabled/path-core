package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when a request is malformed or missing required data.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class BadRequestException extends AccessorUserException {

  /**
   * Build new {@link BadRequestException} with specified description message.
   *
   * @param message message
   */
  public BadRequestException(String message) {
    super(message, PathResponseStatus.BAD_REQUEST);
  }

  /**
   * Build new {@link BadRequestException} with specified description message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public BadRequestException(String message, Throwable cause) {
    super(message, PathResponseStatus.BAD_REQUEST, cause);
  }

  /**
   * Build new {@link BadRequestException} with specified description message and user message.
   *
   * @param message message
   * @param userMessage user message
   */
  @Deprecated
  public BadRequestException(String message, String userMessage) {
    super(message, userMessage, PathResponseStatus.BAD_REQUEST);
  }

  /**
   * Build new {@link BadRequestException} with specified parameters.
   *
   * @param message message
   * @param userMessage user message
   * @param cause cause
   */
  @Deprecated
  public BadRequestException(String message, String userMessage, Throwable cause) {
    super(message, userMessage, PathResponseStatus.BAD_REQUEST, cause);
  }
}
