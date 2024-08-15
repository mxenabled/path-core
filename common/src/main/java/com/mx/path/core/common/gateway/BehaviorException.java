package com.mx.path.core.common.gateway;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown on unrecoverable error in behavior code
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class BehaviorException extends GatewayException {
  public BehaviorException(String message) {
    super(message);
    setInternal(true);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }

  public BehaviorException(String message, Throwable cause) {
    super(message, cause);
    setInternal(true);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
