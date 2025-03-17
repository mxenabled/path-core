package com.mx.path.core.common.accessor;

/**
 * Thrown when a critical value in a response is invalid and no fallback or default is available
 * <br/>
 * These exceptions indicate that the API contract may have changed and will require investigation by MX and the owner
 * of the upstream API.
 */
public class UpstreamResponseValidationException extends UpstreamResponseException {
  public UpstreamResponseValidationException(String message, Throwable cause) {
    super(message, cause);
    withReport(true);
  }

  public UpstreamResponseValidationException(String message) {
    super(message);
    withReport(true);
  }
}
