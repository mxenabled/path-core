package com.mx.path.core.common.facility;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown on unrecoverable error in facility code
 *
 * <p>See {@link PathRequestException} for usage details
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
