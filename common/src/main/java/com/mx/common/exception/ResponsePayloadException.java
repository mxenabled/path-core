package com.mx.common.exception;

/**
 * Thrown when response payload is unrecognizable or un-processable
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
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
