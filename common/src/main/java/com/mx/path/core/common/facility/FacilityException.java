package com.mx.path.core.common.facility;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown on unrecoverable error in facility code.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class FacilityException extends PathRequestException {

  /**
   * Build new {@link FacilityException} with specified message.
   *
   * @param message message
   */
  public FacilityException(String message) {
    super(message);
    setInternal(true);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }

  /**
   * Build new {@link FacilityException} with specified message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public FacilityException(String message, Throwable cause) {
    super(message, cause);
    setInternal(true);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
