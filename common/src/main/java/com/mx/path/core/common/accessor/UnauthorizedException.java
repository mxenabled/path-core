package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when a user attempts an operation when they are not authenticated, the session is expired, or the session is
 * in a bad state.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class UnauthorizedException extends AccessorUserException {

  /**
   * Build new {@link UnauthorizedException} with specified parameters.
   *
   * @param message message
   * @param userMessage user message
   */
  public UnauthorizedException(String message, String userMessage) {
    super(message, userMessage, PathResponseStatus.UNAUTHORIZED);
  }

  /**
   * Build new {@link UnauthorizedException} with specified parameters.
   *
   * @param message message
   * @param userMessage user message
   * @param cause cause
   */
  public UnauthorizedException(String message, String userMessage, Throwable cause) {
    super(message, userMessage, PathResponseStatus.UNAUTHORIZED, cause);
  }
}
