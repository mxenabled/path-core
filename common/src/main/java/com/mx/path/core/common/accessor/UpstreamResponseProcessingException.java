package com.mx.path.core.common.accessor;

/**
 * Thrown when an unknown error occurs while processing the response
 * <br/>
 * This exception indicates that the accessor encountered an internal error while processing a response.
 */
public class UpstreamResponseProcessingException extends UpstreamResponseException {
  public UpstreamResponseProcessingException(String message) {
    super(message);
    withReport(true);
    withStatus(PathResponseStatus.INTERNAL_ERROR);
  }

  public UpstreamResponseProcessingException(String message, Throwable cause) {
    super(message, cause);
    withReport(true);
    withStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
