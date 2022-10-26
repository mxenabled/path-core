package com.mx.common.accessors;

import com.mx.common.exception.PathRequestException;

/**
 * Thrown when an accessor method is invoked that has no implementation
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class AccessorMethodNotImplementedException extends AccessorSystemException {
  public AccessorMethodNotImplementedException() {
    super("Method not implemented");
    setReport(false);
    setStatus(PathResponseStatus.NOT_IMPLEMENTED);
  }
}
