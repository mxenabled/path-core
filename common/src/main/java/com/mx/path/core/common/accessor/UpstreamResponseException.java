package com.mx.path.core.common.accessor;

/**
 * Base class for exceptions related to upstream responses
 */
public abstract class UpstreamResponseException extends AccessorSystemException {
  public UpstreamResponseException(String message) {
    super(message);
    withStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
  }

  public UpstreamResponseException(String message, Throwable cause) {
    super(message, cause);
    withStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
  }
}
