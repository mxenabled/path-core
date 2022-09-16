package com.mx.common.exception;

import com.mx.common.accessors.PathResponseStatus;

/**
 * Thrown on unrecoverable error in Gateway code.
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class GatewayException extends PathRequestException {
  public GatewayException(String message) {
    super(message);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }

  public GatewayException(String message, Throwable cause) {
    super(message, cause);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
