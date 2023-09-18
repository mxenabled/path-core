package com.mx.path.core.common.process;

import lombok.Getter;

import com.mx.path.core.common.exception.PathSystemException;

/**
 * Thrown by {@link RetryConfiguration} when all retries have failed.
 */
public class RetriesFailedException extends PathSystemException {
  @Getter
  private int numberOfFailedAttempts = 0;

  public RetriesFailedException(String message) {
    super(message);
  }

  public RetriesFailedException(Throwable cause) {
    super(cause);
  }

  public RetriesFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public final RetriesFailedException withNumberOfFailedAttempts(int count) {
    this.numberOfFailedAttempts = count;
    return this;
  }
}
