package com.mx.path.core.common.accessor;

/**
 * Thrown when an upstream response payload is not processable (e.g. unexpected format)
 * <br/>
 * Do not use if a payload value is invalid. Use {@link UpstreamResponseValidationException} instead
 */
public class UpstreamResponseUnprocessableException extends UpstreamResponseException {
  public UpstreamResponseUnprocessableException(String message) {
    super(message);
    withReport(true);
  }

  public UpstreamResponseUnprocessableException(String message, Throwable cause) {
    super(message, cause);
    withReport(true);
  }
}
