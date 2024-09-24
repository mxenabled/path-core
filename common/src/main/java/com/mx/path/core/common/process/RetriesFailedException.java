package com.mx.path.core.common.process;

import lombok.Getter;

import com.mx.path.core.common.exception.PathSystemException;

/**
 * Thrown by {@link RetryConfiguration} when all retries have failed.
 */
public class RetriesFailedException extends PathSystemException {

  /**
   * -- GETTER --
   * Return number of failed attempts.
   *
   * @return number of failed attempts
   */
  @Getter
  private int numberOfFailedAttempts = 0;

  /**
   * Build new {@link RetriesFailedException} with specified parameters.
   *
   * @param message message
   */
  public RetriesFailedException(String message) {
    super(message);
  }

  /**
   * Build new {@link RetriesFailedException} with specified parameters.
   *
   * @param cause cause
   */
  public RetriesFailedException(Throwable cause) {
    super(cause);
  }

  /**
   * Build new {@link RetriesFailedException} with specified parameters.
   *
   * @param message message
   * @param cause cause
   */
  public RetriesFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Set number of failed attempts.
   *
   * @param count number to set
   * @return self
   */
  public final RetriesFailedException withNumberOfFailedAttempts(int count) {
    this.numberOfFailedAttempts = count;
    return this;
  }
}
