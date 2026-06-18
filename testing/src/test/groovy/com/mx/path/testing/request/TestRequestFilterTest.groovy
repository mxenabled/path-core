package com.mx.path.testing.request

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import com.mx.path.core.common.connect.Request
import com.mx.path.core.common.connect.Response

import org.spockframework.runtime.SpockAssertionError

import spock.lang.Specification

class TestRequestFilterTest extends Specification {

  def cleanup() {
    RequestExpectations.reset()
  }

  def makeRequest(String path = "accounts", String method = "GET") {
    def req = mock(Request)
    when(req.getPath()).thenReturn(path)
    when(req.getMethod()).thenReturn(method)
    when(req.getBaseUrl()).thenReturn("http://localhost")
    return req
  }

  def "execute invokes matching expectation handler"() {
    given:
    def handlerCalled = false
    def filter = new TestRequestFilter()
    def matcher = RequestMatcher.Fluent.withPath("accounts")
    RequestExpectations.Fluent.expectRequest(matcher)
        .toRespond({ req, resp -> handlerCalled = true })

    when:
    filter.execute(makeRequest("accounts"), mock(Response))

    then:
    handlerCalled
  }

  def "execute throws when no handler registered"() {
    given:
    def filter = new TestRequestFilter()

    when:
    filter.execute(makeRequest("unknown"), mock(Response))

    then:
    thrown(SpockAssertionError)
  }

  def "execute throws when multiple handlers match"() {
    given:
    def filter = new TestRequestFilter()
    RequestExpectations.Fluent.expectRequest(new RequestMatcher())
        .toRespond({ req, resp -> })
    RequestExpectations.Fluent.expectRequest(new RequestMatcher())
        .toRespond({ req, resp -> })

    when:
    filter.execute(makeRequest(), mock(Response))

    then:
    thrown(SpockAssertionError)
  }

  def "describe returns empty string when no expectations"() {
    given:
    def filter = new TestRequestFilter()

    expect:
    filter.describe() == ""
  }

  def "describe includes expectation descriptions when set"() {
    given:
    def filter = new TestRequestFilter()
    RequestExpectations.Fluent.expectRequest(RequestMatcher.Fluent.withPath("accounts"))
        .toRespond({ req, resp -> })

    when:
    def desc = filter.describe()

    then:
    desc.contains("accounts")
  }

  def "allowance matcher also satisfies execute"() {
    given:
    def filter = new TestRequestFilter()
    RequestExpectations.Fluent.stubRequest(RequestMatcher.Fluent.withPath("accounts"))
        .toRespond({ req, resp -> })

    when:
    filter.execute(makeRequest("accounts"), mock(Response))

    then:
    noExceptionThrown()
  }
}
