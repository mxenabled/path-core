package com.mx.common.process;

import lombok.Getter;

import com.mx.common.http.HttpStatus;

public enum FaultTolerantExecutionFailureStatus {
  TASK_LIMIT_EXCEEDED(429, "Task limit exceeded"),
  INTERNAL_ERROR(500, "Internal error"),
  TASK_EXECUTION_UNAVAILABLE(503, "Task execution unavailable"),
  TASK_TIMEOUT(504, "Task timed out");

  @Getter
  private final int value;

  @Getter
  private final String description;

  FaultTolerantExecutionFailureStatus(int value, String description) {
    this.value = value;
    this.description = description;
  }

  public HttpStatus toHttpStatus() {
    return HttpStatus.resolve(value);
  }
}
