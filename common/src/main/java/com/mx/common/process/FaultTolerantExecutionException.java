package com.mx.common.process;

import lombok.Getter;

public class FaultTolerantExecutionException extends RuntimeException {
  @Getter
  private final FaultTolerantExecutionFailureStatus status;

  public FaultTolerantExecutionException(FaultTolerantExecutionFailureStatus status) {
    this.status = status;
  }

  public FaultTolerantExecutionException(String message, FaultTolerantExecutionFailureStatus status) {
    super(message);
    this.status = status;
  }

  public FaultTolerantExecutionException(String message, Throwable cause, FaultTolerantExecutionFailureStatus status) {
    super(message, cause);
    this.status = status;
  }

  public FaultTolerantExecutionException(Throwable cause, FaultTolerantExecutionFailureStatus status) {
    super(cause);
    this.status = status;
  }

  public FaultTolerantExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, FaultTolerantExecutionFailureStatus status) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.status = status;
  }
}
