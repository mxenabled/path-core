package com.mx.common.exception;

/**
 * Thrown when request payload cannot be built
 *
 * <p>
 *   See {@link PathRequestException} for usage details
 * </p>
 */
public class RequestPayloadException extends AccessorSystemException {
  public RequestPayloadException(String message) {
    super(message);
    setReport(true);
  }

  public RequestPayloadException(String message, Throwable cause) {
    super(message, cause);
    setReport(true);
  }
}
