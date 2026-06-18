package com.mx.path.testing.request

import static org.mockito.Mockito.mock

import com.mx.path.core.common.connect.Request
import com.mx.path.core.common.connect.Response
import com.mx.path.testing.request.RequestExpectation
import com.mx.path.testing.request.RequestMatcher

import org.spockframework.runtime.SpockAssertionError

import spock.lang.Specification

class RequestExpectationTest extends Specification {

  def cleanup() {
    RequestExpectations.reset()
  }

  def makeRequest(String path = "accounts") {
    def req = mock(Request)
    org.mockito.Mockito.when(req.getPath()).thenReturn(path)
    return req
  }

  def makeResponse() {
    return mock(Response)
  }

  def "toRespond sets handler"() {
    given:
    def expectation = new RequestExpectation(RequestMatcher.Fluent.withPath("accounts"))

    when:
    expectation.toRespond({ req, resp -> })

    then:
    expectation.getHandler() != null
  }

  def "handle invokes the handler and increments invokeCount"() {
    given:
    def expectation = new RequestExpectation(RequestMatcher.Fluent.withPath("accounts"))
    def handled = false
    expectation.toRespond({ req, resp -> handled = true })

    when:
    expectation.handle(makeRequest(), makeResponse())

    then:
    handled
    expectation.getInvokeCount() == 1
  }

  def "assertInvoked passes when invokeCount matches expectedInvokeCount"() {
    given:
    def expectation = new RequestExpectation(RequestMatcher.Fluent.withPath("accounts"))
    expectation.toRespond({ req, resp -> })
    expectation.handle(makeRequest(), makeResponse())

    when:
    expectation.assertInvoked()

    then:
    noExceptionThrown()
  }

  def "assertInvoked throws when invokeCount does not match"() {
    given:
    def expectation = new RequestExpectation(RequestMatcher.Fluent.withPath("accounts"))
    expectation.toRespond({ req, resp -> })

    when:
    expectation.assertInvoked()

    then:
    thrown(SpockAssertionError)
  }

  def "times sets expectedInvokeCount"() {
    given:
    def expectation = new RequestExpectation(RequestMatcher.Fluent.withPath("accounts"))
    expectation.toRespond({ req, resp -> })

    when:
    expectation.times(2)
    expectation.handle(makeRequest(), makeResponse())
    expectation.handle(makeRequest(), makeResponse())
    expectation.assertInvoked()

    then:
    noExceptionThrown()
    expectation.getInvokeCount() == 2
  }

  def "getReceivedRequest returns first handled request"() {
    given:
    def expectation = new RequestExpectation(RequestMatcher.Fluent.withPath("accounts"))
    def request = makeRequest()
    expectation.toRespond({ req, resp -> })
    expectation.handle(request, makeResponse())

    when:
    def received = expectation.getReceivedRequest()

    then:
    received.is(request)
  }

  def "getReceivedRequests returns all handled requests"() {
    given:
    def expectation = new RequestExpectation(new RequestMatcher())
    expectation.times(2)
    expectation.toRespond({ req, resp -> })
    expectation.handle(makeRequest(), makeResponse())
    expectation.handle(makeRequest(), makeResponse())

    when:
    def received = expectation.getReceivedRequests()

    then:
    received.size() == 2
  }

  def "collectRequests adds handled requests to RequestExpectations"() {
    given:
    def expectation = new RequestExpectation(new RequestMatcher())
    expectation.toRespond({ req, resp -> })
    expectation.collectRequests()

    when:
    expectation.handle(makeRequest(), makeResponse())

    then:
    RequestExpectations.requests().size() == 1
  }

  def "onRequest callback is invoked on each handle"() {
    given:
    def expectation = new RequestExpectation(RequestMatcher.Fluent.withPath("accounts"))
    def callCount = 0
    expectation.toRespond({ req, resp -> })
    expectation.onRequest({ req, resp -> callCount++ })

    when:
    expectation.handle(makeRequest(), makeResponse())

    then:
    callCount == 1
  }
}
