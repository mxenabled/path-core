package com.mx.common.exception.request.accessor;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.exception.request.PathRequestException;

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
