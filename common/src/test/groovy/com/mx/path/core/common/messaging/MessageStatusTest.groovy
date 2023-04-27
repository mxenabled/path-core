package com.mx.path.core.common.messaging


import com.mx.path.core.common.http.HttpStatus

import spock.lang.Specification
import spock.lang.Unroll

class MessageStatusTest extends Specification {

  @Unroll
  def "fromHttpStatus"() {
    when:
    def result = MessageStatus.fromHttpStatus(httpStatus)

    then:
    result == expected

    where:
    httpStatus                  || expected
    HttpStatus.OK || MessageStatus.SUCCESS
    HttpStatus.REQUEST_TIMEOUT  || MessageStatus.TIMEOUT
    HttpStatus.ALREADY_REPORTED || null
  }

  @Unroll
  def "toHttpStatus"() {
    when:
    def result = messageStatus.toHttpStatus()

    then:
    result == expected

    where:
    messageStatus         || expected
    MessageStatus.SUCCESS || HttpStatus.OK
    MessageStatus.TIMEOUT || HttpStatus.REQUEST_TIMEOUT
  }
}
