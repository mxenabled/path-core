package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when the upstream system is determined to be offline for maintenance.
 *
 * <p>This would typically be thrown after an upstream indicates that the system is offline. This is not always
 * possible to determine based on call results, in which case a {@link UpstreamSystemUnavailable} exception should
 * be used as a fallback.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class UpstreamSystemMaintenance extends UpstreamSystemUnavailable {
  public UpstreamSystemMaintenance(String message) {
    super(message);
    setErrorTitle("System under maintenance");
  }

  public UpstreamSystemMaintenance(String message, Throwable cause) {
    super(message, cause);
    setErrorTitle("System under maintenance");
  }

  @Deprecated
  public UpstreamSystemMaintenance(String message, String userMessage) {
    super(message, userMessage);
    setErrorTitle("System under maintenance");
  }

  @Deprecated
  public UpstreamSystemMaintenance(String message, String userMessage, Throwable cause) {
    super(message, userMessage, cause);
    setErrorTitle("System under maintenance");
  }
}
