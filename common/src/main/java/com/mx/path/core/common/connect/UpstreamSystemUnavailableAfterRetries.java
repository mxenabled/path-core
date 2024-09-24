package com.mx.path.core.common.connect;

import com.mx.path.core.common.accessor.UpstreamSystemUnavailable;

/**
 * Thrown by {@link Request} when upstream service is unavailable after retries.
 */
public class UpstreamSystemUnavailableAfterRetries extends UpstreamSystemUnavailable {

  /**
   * Build new {@link UpstreamSystemUnavailableAfterRetries} with specified message.
   *
   * @param message message
   */
  public UpstreamSystemUnavailableAfterRetries(String message) {
    super(message);
  }

  /**
   * Build new {@link UpstreamSystemUnavailableAfterRetries} with specified message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public UpstreamSystemUnavailableAfterRetries(String message, Throwable cause) {
    super(message, cause);
  }
}
