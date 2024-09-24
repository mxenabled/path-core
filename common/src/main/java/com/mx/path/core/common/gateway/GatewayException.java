package com.mx.path.core.common.gateway;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown on unrecoverable error in Gateway code.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class GatewayException extends PathRequestException {

  /**
   * Build new {@link GatewayException} with specified parameters.
   *
   * @param message message
   */
  public GatewayException(String message) {
    super(message);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }

  /**
   * Build new {@link GatewayException} with specified parameters.
   *
   * @param message message
   * @param cause cause
   */
  public GatewayException(String message, Throwable cause) {
    super(message, cause);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
