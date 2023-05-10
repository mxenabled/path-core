package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Base class for exceptions thrown on unrecoverable error in accessor code
 *
 * <p>See {@link PathRequestException} for usage details
 */
public abstract class AccessorSystemException extends AccessorException {
  public AccessorSystemException(String message) {
    super(message);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }

  public AccessorSystemException(String message, Throwable cause) {
    super(message, cause);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
