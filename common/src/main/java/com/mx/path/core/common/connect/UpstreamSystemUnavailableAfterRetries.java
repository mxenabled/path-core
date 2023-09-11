package com.mx.path.core.common.connect;

import com.mx.path.core.common.accessor.UpstreamSystemUnavailable;

/**
 * Thrown by {@link Request} when upstream service is unavailable after retries.
 */
public class UpstreamSystemUnavailableAfterRetries extends UpstreamSystemUnavailable {
  public UpstreamSystemUnavailableAfterRetries(String message) {
    super(message);
  }

  public UpstreamSystemUnavailableAfterRetries(String message, Throwable cause) {
    super(message, cause);
  }
}
