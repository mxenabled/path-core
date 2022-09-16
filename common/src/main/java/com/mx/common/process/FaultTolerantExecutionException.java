package com.mx.common.process;

import lombok.Getter;

import com.mx.common.accessors.PathResponseStatus;

public class FaultTolerantExecutionException extends RuntimeException {
  @Getter
  private final PathResponseStatus status;

  public FaultTolerantExecutionException(PathResponseStatus status) {
    this.status = status;
  }

  public FaultTolerantExecutionException(String message, PathResponseStatus status) {
    super(message);
    this.status = status;
  }

  public FaultTolerantExecutionException(String message, Throwable cause, PathResponseStatus status) {
    super(message, cause);
    this.status = status;
  }

  public FaultTolerantExecutionException(Throwable cause, PathResponseStatus status) {
    super(cause);
    this.status = status;
  }

  public FaultTolerantExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, PathResponseStatus status) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.status = status;
  }
}
