package com.mx.common.gateway;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.exception.PathRequestException;

/**
 * Thrown on unrecoverable error in Gateway code.
 *
 * <p>See {@link PathRequestException} for usage details
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
