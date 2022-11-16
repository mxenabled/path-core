package com.mx.common.accessors;

import com.mx.common.exception.PathRequestException;

/**
 * Thrown when an upstream request results in an unknown error or the upstream service is determined to be offline.
 *
 * <p>This can be used as a catch-all exception for situations where something went bad upstream,
 * usually resulting in an unexpected status code, and we couldn't figure out why. This is close relative to
 * {@link ResponsePayloadException}. The difference is that {@link ResponsePayloadException} indicates that we were
 * unable to process a response that was expected to be good. This exception is a bit more general and used when
 * we can determine that the response is not right.
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
 *     thrown new UpstreamSystemUnavailable("Bank system unknown state", "Bank system failed to process request. Please try again later.")
 *   }
 * }
 * }</pre>
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class UpstreamSystemUnavailable extends AccessorSystemException {
  public UpstreamSystemUnavailable(String message, String userMessage) {
    super(message);
    setReport(false);
    setStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
    setUserMessage(userMessage);
  }

  public UpstreamSystemUnavailable(String message, String userMessage, Throwable cause) {
    super(message, cause);
    setReport(false);
    setStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
    setUserMessage(userMessage);
  }
}
