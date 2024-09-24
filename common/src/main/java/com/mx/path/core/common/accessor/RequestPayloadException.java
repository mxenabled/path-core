package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when an upstream request payload cannot be built
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class RequestPayloadException extends AccessorSystemException {

  /**
   * Build new {@link RequestPayloadException} with specified description message.
   *
   * @param message message
   */
  public RequestPayloadException(String message) {
    super(message);
    setInternal(true);
    setReport(true);
  }

  /**
   * Build new {@link RequestPayloadException} with specified description message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public RequestPayloadException(String message, Throwable cause) {
    super(message, cause);
    setInternal(true);
    setReport(true);
  }
}
