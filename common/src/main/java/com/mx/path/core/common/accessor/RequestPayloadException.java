package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when an upstream request payload cannot be built
 *
 * <p>See {@link PathRequestException} for usage details
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
