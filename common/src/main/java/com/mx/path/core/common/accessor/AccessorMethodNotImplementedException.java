package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when an accessor method invoked has no implementation.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class AccessorMethodNotImplementedException extends AccessorSystemException {

  /**
   * Default constructor.
   */
  public AccessorMethodNotImplementedException() {
    super("Method not implemented");
    setInternal(true);
    setReport(false);
    setStatus(PathResponseStatus.NOT_IMPLEMENTED);
  }
}
