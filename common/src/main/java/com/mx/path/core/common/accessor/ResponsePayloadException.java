package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when an upstream response payload is unrecognizable or un-processable
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class ResponsePayloadException extends AccessorSystemException {

  /**
   * Build new {@link ResponsePayloadException} with specified parameters.
   *
   * @param message message
   */
  public ResponsePayloadException(String message) {
    super(message);
    setInternal(true);
    setReport(true);
  }

  /**
   * Build new {@link ResponsePayloadException} with specified parameters.
   *
   * @param message message
   * @param cause cause
   */
  public ResponsePayloadException(String message, Throwable cause) {
    super(message, cause);
    setInternal(true);
    setReport(true);
  }
}
