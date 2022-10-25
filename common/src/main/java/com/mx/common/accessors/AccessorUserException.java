package com.mx.common.accessors;

import com.mx.common.exception.PathRequestException;

/**
 * Thrown on user-related errors in accessor code
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class AccessorUserException extends AccessorException {
  public AccessorUserException(String message, String userMessage, PathResponseStatus status) {
    super(message);
    setStatus(status);
    setReport(false);
    setUserMessage(userMessage);
  }

  public AccessorUserException(String message, String userMessage, PathResponseStatus status, Throwable cause) {
    super(message, cause);
    setStatus(status);
    setReport(false);
    setUserMessage(userMessage);
  }
}
