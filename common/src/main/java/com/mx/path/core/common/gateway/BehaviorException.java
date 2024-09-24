package com.mx.path.core.common.gateway;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown on unrecoverable error in behavior code.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class BehaviorException extends GatewayException {

  /**
   * Build new {@link BehaviorException} with specified description message.
   *
   * @param message message
   */
  public BehaviorException(String message) {
    super(message);
    setInternal(true);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }

  /**
   * Build new {@link BehaviorException} with specified message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public BehaviorException(String message, Throwable cause) {
    super(message, cause);
    setInternal(true);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
