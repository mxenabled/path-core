package com.mx.common.exception;

import com.mx.common.accessors.PathResponseStatus;

/**
 * Thrown on unrecoverable error in accessor code
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class AccessorSystemException extends AccessorException {
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
