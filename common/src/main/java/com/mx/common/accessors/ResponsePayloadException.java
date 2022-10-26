package com.mx.common.accessors;

import com.mx.common.exception.PathRequestException;

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
