package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Base class for exceptions thrown on user-related errors in accessor code.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public abstract class AccessorUserException extends AccessorException {

  /**
   * Build new {@link AccessorUserException} with specified parameters.
   *
   * @param message message
   * @param status status
   */
  public AccessorUserException(String message, PathResponseStatus status) {
    super(message);
    setStatus(status);
    setReport(false);
  }

  /**
   * Build new {@link AccessorUserException} with specified parameters.
   *
   * @param message message
   * @param status status
   * @param cause cause
   */
  public AccessorUserException(String message, PathResponseStatus status, Throwable cause) {
    super(message, cause);
    setStatus(status);
    setReport(false);
  }

  /**
   * Build new {@link AccessorUserException} with specified parameters.
   *
   * @param message message
   * @param userMessage user message
   * @param status status
   */
  public AccessorUserException(String message, String userMessage, PathResponseStatus status) {
    super(message);
    setStatus(status);
    setReport(false);
    setUserMessage(userMessage);
  }

  /**
   * Build new {@link AccessorUserException} with specified parameters.
   *
   * @param message message
   * @param userMessage user message
   * @param status status
   * @param cause cause
   */
  public AccessorUserException(String message, String userMessage, PathResponseStatus status, Throwable cause) {
    super(message, cause);
    setStatus(status);
    setReport(false);
    setUserMessage(userMessage);
  }
}
