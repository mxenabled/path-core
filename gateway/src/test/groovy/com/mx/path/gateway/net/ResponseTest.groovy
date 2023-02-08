package com.mx.path.gateway.net

import com.mx.common.accessors.AccessorSystemException
import com.mx.common.accessors.BadRequestException
import com.mx.common.accessors.PathResponseStatus
import com.mx.common.accessors.UpstreamSystemUnavailable
import com.mx.common.collections.MultiValueMap
import com.mx.common.http.HttpStatus
import com.mx.path.model.context.RequestContext

import spock.lang.Specification

class ResponseTest extends Specification {
  Response subject

  def setup() {
    subject = new Response()
    RequestContext.builder().clientId("hughes").build().register()
  }

  def "testBody"() {
    given:
    subject.withBody("{}")

    expect:
    subject.getBody() == "{}"
  }

  def "testRawBody"() {
    given:
    Random rd = new Random()
    byte[] bytes = new byte[7]
    rd.nextBytes(bytes)
    subject.withRawData(bytes)

    expect:
    subject.getRawBody() == bytes
  }

  def "testNullBodyReturnsFalse"() {
    given:
    subject.withBody(null)

    expect:
    !subject.hasBody()
  }

  def "testHasBody"() {
    given:
    subject.withBody("{}")

    expect:
    subject.hasBody()
  }

  def "testCookies"() {
    given:
    def cookies = new MultiValueMap()
    cookies.add("cookie", "value")
    subject.withCookies(cookies)

    expect:
    subject.getCookies().get("cookie") == "value"
  }

  def "testHeaders"() {
    given:
    def headers = new MultiValueMap()
    headers.add("header", "value")
    subject.withHeaders(headers)

    expect:
    subject.getHeaders().get("header") == "value"
  }

  def "testRequest"() {
    given:
    def request = new Request()
    subject.withRequest(request)

    expect:
    subject.getRequest() == request
  }

  def "testStatus"() {
    given:
    subject.withStatus(HttpStatus.OK)

    expect:
    subject.getStatus() == HttpStatus.OK
  }

  def "testCheckStatusThrowsException"() {
    given:
    def originalException = new Exception("error")
    subject.withException(originalException)

    when:
    subject.checkStatus()

    then:
    def e = thrown(UpstreamSystemUnavailable)
    e.getMessage() == "Request threw an exception"
    e.getCause() == originalException
  }

  def "testCheckStatusThrowsExceptionWithSupplier"() {
    given:
    subject.status = HttpStatus.INTERNAL_SERVER_ERROR

    when:
    subject.checkStatus({ -> throw new BadRequestException("Bad request", "Bad request") })

    then:
    def e = thrown(BadRequestException)
    e.getMessage() == "Bad request"
  }

  def "testCheckStatusThrowsExceptionWithSupplier - with exception"() {
    given:
    def originalException = new Exception("error")
    subject.withException(originalException)
    when:
    subject.checkStatus({ -> throw new BadRequestException("Bad request", "Bad request") })

    then:
    def e = thrown(UpstreamSystemUnavailable)
    e.getMessage() == "Request threw an exception"
  }

  def "testCheckStatusThrows500WhenNullStatus"() {
    given:
    subject.withStatus(null)

    when:
    subject.checkStatus()

    then:
    def e = thrown(UpstreamSystemUnavailable)
    e.getMessage() == "Request threw an exception"
  }

  def "throwException wraps non-PathRequestException"() {
    setup:
    def exception = new Exception("error")
    subject.withException(exception)

    when:
    subject.throwException()

    then:
    def ex = thrown(AccessorSystemException)
    ex.getCause() == exception
  }

  def "throwException throws Exception"() {
    setup:
    def cause = new Exception()
    def exception = new BadRequestException("Something happened", "User Message", cause)
    subject.withException(exception)

    when:
    subject.throwException()

    then:
    def ex = thrown(BadRequestException)
    ex == exception
    ex.getCause() == cause
    ex.getStatus() == PathResponseStatus.BAD_REQUEST
    ex.getMessage() == "Something happened"
    !ex.shouldReport()
  }
}
