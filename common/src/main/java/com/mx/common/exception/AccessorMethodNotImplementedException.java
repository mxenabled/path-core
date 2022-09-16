package com.mx.common.exception;

import com.mx.common.accessors.PathResponseStatus;

/**
 * Thrown when an accessor method is invoked that has no implementation.
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class AccessorMethodNotImplementedException extends AccessorSystemException {
  public AccessorMethodNotImplementedException() {
    super("Method not implemented");
    setReport(false);
    setStatus(PathResponseStatus.NOT_IMPLEMENTED);
  }
}
