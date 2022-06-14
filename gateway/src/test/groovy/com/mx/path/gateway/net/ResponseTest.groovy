package com.mx.path.gateway.net


import com.mx.common.collections.MultiValueMap
import com.mx.common.collections.SingleValueMap
import com.mx.common.http.HttpStatus
import com.mx.path.gateway.util.MdxApiException
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

  def "testCheckStatusThrowsMdxApiException"() {
    given:
    def originalException = new Exception("error")
    subject.withException(originalException)

    when:
    subject.checkStatus()

    then:
    def e = thrown(MdxApiException.class)
    e.getMessage() == "Request threw an exception"
    e.getCause() == originalException
  }
  def "testCheckStatusThrowsMdxApiExceptionWithSupplier"(){
    given:
    def originalException = new Exception("error")
    subject.withException(originalException)
    when:
    subject.checkStatus( {
      ->
      throw new MdxApiException(HttpStatus.BAD_REQUEST)
    })

    then:
    def e = thrown(MdxApiException.class)
    e.getMessage() == "Request threw an exception"
  }
  def "testCheckStatusThrows500WhenNullStatus"(){
    given:
    subject.withStatus(null)

    when:
    subject.checkStatus()

    then:
    def e = thrown(MdxApiException.class)
    e.getMessage() == "Request threw an exception"
  }
  def "throwException wraps non-MdxApiException"() {
    setup:
    def exception = new Exception("error")
    subject.withException(exception)

    when:
    subject.throwException()

    then:
    def ex = thrown(MdxApiException)
    ex.getCause() == exception
  }

  def "throwException throws MdxApiException"() {
    setup:
    def cause = new Exception()
    def exception = new MdxApiException("Something happened", HttpStatus.INTERNAL_SERVER_ERROR, true, cause)
    subject.withException(exception)

    when:
    subject.throwException()

    then:
    def ex = thrown(MdxApiException)
    ex == exception
    ex.getCause() == cause
    ex.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR
    ex.getMessage() == "Something happened"
    ex.shouldReport()
  }
}
