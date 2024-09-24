package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Base class for exceptions thrown by accessors.
 *
 * <p>See {@link PathRequestException} for usage instructions
 */
public abstract class AccessorException extends PathRequestException {
  private static final long serialVersionUID = 1L;

  /**
   * Build new {@link AccessorException} with specified path status.
   *
   * @param status status
   */
  public AccessorException(PathResponseStatus status) {
    super();
    setStatus(status);
  }

  /**
   * Build new {@link AccessorException} with specified description message.
   *
   * @param message message
   */
  public AccessorException(String message) {
    super(message);
  }

  /**
   * Build new {@link AccessorException} with specified cause.
   *
   * @param cause cause
   */
  public AccessorException(Throwable cause) {
    super(cause);
  }

  /**
   * Build new {@link AccessorException} with specified description message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public AccessorException(String message, Throwable cause) {
    super(message, cause);
  }
}
