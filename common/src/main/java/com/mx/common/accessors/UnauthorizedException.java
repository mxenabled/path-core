package com.mx.common.accessors;

import com.mx.common.exception.PathRequestException;

/**
 * Thrown when a user attempts an operation when they are not authenticated, the session is expired, or the session is
 * in a bad state.
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class UnauthorizedException extends AccessorUserException {
  public UnauthorizedException(String message, String userMessage) {
    super(message, userMessage, PathResponseStatus.UNAUTHORIZED);
  }

  public UnauthorizedException(String message, String userMessage, Throwable cause) {
    super(message, userMessage, PathResponseStatus.UNAUTHORIZED, cause);
  }
}
