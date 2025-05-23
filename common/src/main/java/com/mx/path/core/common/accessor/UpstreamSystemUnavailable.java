package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when an upstream request results in an unknown error or the upstream service is determined to be offline.
 *
 * <p>This can be used as a catch-all exception for situations where something went bad upstream,
 * usually resulting in an unexpected status code, and we couldn't figure out why. This is close relative to
 * children of {@link UpstreamResponseException}. The difference is that {@link UpstreamResponseException} indicates
 * specific reasons that we were unable to process a response that was expected to be good.
 *
 * <p>Consider the following code example. In this example, we are attempting to determine why the response did not have
 * a 200 OK status. Keep in mind, this is upstream-system-specific. Determining what happened, may not
 * be possible, in which case, we do our best.
 *
 * <pre>{@code
 * if (response.getStatus() != HttpStatus.OK) {
 *   String code = extractErrorCodeFromResponseBody(response.getBody());
 *
 *   if ("validation".equals(code)) {
 *     thrown new RequestValidationException("Invalid request", "The request was not valid. Please review and try again. If this error occurs again, please call customer service.")
 *   } else if ("maintenance".equals(code)) {
 *     thrown new UpstreamSystemMaintenance("Bank system maintenance", "Bank systems are down for maintenance. Please try again later.")
 *   } else if ("internal".equals(code)) {
 *     thrown new UpstreamSystemUnavailable("Bank system error", "Bank system failed to process request. Please try again later.")
 *   } else { // default
 *     thrown new UpstreamResponseValidationException("Bank system unknown state", "Bank system failed to process request. Please try again later.")
 *   }
 * }
 * }</pre>
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class UpstreamSystemUnavailable extends AccessorSystemException {

  /**
   * Build new {@link UpstreamSystemUnavailable} with specified parameters.
   *
   * @param message message
   */
  public UpstreamSystemUnavailable(String message) {
    super(message);
    setReport(false);
    setStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
  }

  /**
   * Build new {@link UpstreamSystemUnavailable} with specified parameters.
   *
   * @param message message
   * @param cause cause
   */
  public UpstreamSystemUnavailable(String message, Throwable cause) {
    super(message, cause);
    setReport(false);
    setStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
  }

  /**
   * Build new {@link UpstreamSystemUnavailable} with specified parameters.
   *
   * @param message message
   * @param userMessage user message
   */
  @Deprecated
  public UpstreamSystemUnavailable(String message, String userMessage) {
    super(message);
    setReport(false);
    setStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
    setUserMessage(userMessage);
  }

  /**
   * Build new {@link UpstreamSystemUnavailable} with specified parameters.
   *
   * @param message message
   * @param userMessage user message
   * @param cause cause
   */
  @Deprecated
  public UpstreamSystemUnavailable(String message, String userMessage, Throwable cause) {
    super(message, cause);
    setReport(false);
    setStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
    setUserMessage(userMessage);
  }
}
