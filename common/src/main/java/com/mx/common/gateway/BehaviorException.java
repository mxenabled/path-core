package com.mx.common.gateway;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.exception.PathRequestException;

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
