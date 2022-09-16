package com.mx.common.exception;

import com.mx.common.accessors.PathResponseStatus;

/**
 * Base class for exceptions thrown by accessors
 *
 * <p>
 * See {@link PathRequestException} for usage instructions
 * </p>
 */
public abstract class AccessorException extends PathRequestException {
  private static final long serialVersionUID = 1L;

  public AccessorException(PathResponseStatus status) {
    super();
    setStatus(status);
  }

  public AccessorException(String message) {
    super(message);
  }

  public AccessorException(Throwable cause) {
    super(cause);
  }

  public AccessorException(String message, Throwable cause) {
    super(message, cause);
  }
}
