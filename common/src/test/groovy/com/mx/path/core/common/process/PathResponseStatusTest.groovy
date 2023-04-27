package com.mx.path.core.common.process

import com.mx.path.core.common.accessor.PathResponseStatus
import com.mx.path.core.common.http.HttpStatus

import spock.lang.Specification
import spock.lang.Unroll

class PathResponseStatusTest extends Specification {
  @Unroll
  def "toHttpStatus"() {
    when:
    def result = pathResponseStatus.toHttpStatus()

    then:
    result == expected

    where:
    pathResponseStatus                   || expected
    PathResponseStatus.TOO_MANY_REQUESTS || HttpStatus.TOO_MANY_REQUESTS
    PathResponseStatus.INTERNAL_ERROR    || HttpStatus.INTERNAL_SERVER_ERROR
    PathResponseStatus.UNAVAILABLE       || HttpStatus.SERVICE_UNAVAILABLE
    PathResponseStatus.TIMEOUT           || HttpStatus.GATEWAY_TIMEOUT
  }
}
