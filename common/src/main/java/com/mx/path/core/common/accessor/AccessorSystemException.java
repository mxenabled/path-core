package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Base class for exceptions thrown on unrecoverable error in accessor code.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public abstract class AccessorSystemException extends AccessorException {

  /**
   * Build new {@link AccessorSystemException} with specified description message.
   *
   * @param message message
   */
  public AccessorSystemException(String message) {
    super(message);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }

  /**
   * Build new {@link AccessorSystemException} with specified description message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public AccessorSystemException(String message, Throwable cause) {
    super(message, cause);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
