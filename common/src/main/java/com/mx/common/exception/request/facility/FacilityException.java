package com.mx.common.exception.request.facility;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.exception.request.PathRequestException;

/**
 * Thrown on unrecoverable error in facility code
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class FacilityException extends PathRequestException {
  public FacilityException(String message) {
    super(message);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }

  public FacilityException(String message, Throwable cause) {
    super(message, cause);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
