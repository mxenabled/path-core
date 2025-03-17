package com.mx.path.core.common.accessor;

/**
 * Use when the upstream response is an error that is not user-correctable
 * <br/>
 * This indicates that we have determined that the response represents an error from the upstream-side that is not
 * due to request validation.
 */
public class UpstreamResponseError extends UpstreamResponseException {
  public UpstreamResponseError(String message) {
    super(message);
  }

  public UpstreamResponseError(String message, Throwable cause) {
    super(message, cause);
  }
}
