package com.mx.common.process

import com.mx.common.http.HttpStatus

import spock.lang.Specification
import spock.lang.Unroll

class FaultTolerantExecutionFailureStatusTest extends Specification {
  @Unroll
  def "toHttpStatus"() {
    when:
    def result = faultTolerantExecutionFailureStatus.toHttpStatus()

    then:
    result == expected

    where:
    faultTolerantExecutionFailureStatus                            || expected
    FaultTolerantExecutionFailureStatus.TASK_LIMIT_EXCEEDED        || HttpStatus.TOO_MANY_REQUESTS
    FaultTolerantExecutionFailureStatus.INTERNAL_ERROR             || HttpStatus.INTERNAL_SERVER_ERROR
    FaultTolerantExecutionFailureStatus.TASK_EXECUTION_UNAVAILABLE || HttpStatus.SERVICE_UNAVAILABLE
    FaultTolerantExecutionFailureStatus.TASK_TIMEOUT               || HttpStatus.GATEWAY_TIMEOUT
  }
}
