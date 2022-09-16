package com.mx.common.exception;

import com.mx.common.accessors.PathResponseStatus;

/**
 * Thrown on unrecoverable error in behavior code
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class BehaviorException extends GatewayException {
  public BehaviorException(String message) {
    super(message);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }

  public BehaviorException(String message, Throwable cause) {
    super(message, cause);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
