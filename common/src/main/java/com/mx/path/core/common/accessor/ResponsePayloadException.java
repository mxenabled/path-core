package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when an upstream response payload is unrecognizable or un-processable
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class ResponsePayloadException extends AccessorSystemException {
  public ResponsePayloadException(String message) {
    super(message);
    setReport(true);
  }

  public ResponsePayloadException(String message, Throwable cause) {
    super(message, cause);
    setReport(true);
  }
}
