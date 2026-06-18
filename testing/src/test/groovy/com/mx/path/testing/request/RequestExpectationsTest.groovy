package com.mx.path.testing.request

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import com.mx.path.gateway.connect.filter.CallbacksFilter
import com.mx.path.gateway.connect.filter.ErrorHandlerFilter
import com.mx.path.gateway.connect.filter.RequestFinishedFilter
import com.mx.testing.TestConnection

import spock.lang.Specification

class RequestExpectationsTest extends Specification {
  def cleanup() {
    RequestExpectations.reset()
  }

  def "expectConnection"() {
    when:
    RequestExpectations.expectConnection(RequestMatcher.Fluent.withPath("path"))

    then: "should not be empty"
    RequestExpectations.getRequestAllowances().size() == 0
    RequestExpectations.getRequestExpectations().size() == 1
  }

  def "allowConnection"() {
    when:
    RequestExpectations.allowConnection(RequestMatcher.Fluent.withPath("path"))

    then: "should not be empty"
    RequestExpectations.getRequestAllowances().size() == 1
  }

  def "stubConnection"() {
    when:
    RequestExpectations.stubConnection(RequestMatcher.Fluent.withPath("path"))

    then: "should not be empty"
    RequestExpectations.getRequestAllowances().size() == 1
  }

  def "reset"() {
    when:
    RequestExpectations.expectConnection(RequestMatcher.Fluent.withPath("path"))
    RequestExpectations.allowConnection(RequestMatcher.Fluent.withPath("path"))
    RequestExpectations.stubConnection(RequestMatcher.Fluent.withPath("path"))

    then: "should not be empty"
    RequestExpectations.getRequestAllowances().size() == 2
    RequestExpectations.getRequestExpectations().size() == 1

    when: "reset"
    RequestExpectations.reset()

    then: "should be empty"
    RequestExpectations.getRequestAllowances().isEmpty()
    RequestExpectations.getRequestExpectations().isEmpty()
  }

  def "addRequest and request() retrieval"() {
    given:
    def req = mock(com.mx.path.core.common.connect.Request)
    when(req.getPath()).thenReturn("accounts")

    when:
    RequestExpectations.addRequest(req)

    then:
    RequestExpectations.request() != null
    RequestExpectations.request().is(req)
  }

  def "requests() returns all added requests"() {
    given:
    def req1 = mock(com.mx.path.core.common.connect.Request)
    def req2 = mock(com.mx.path.core.common.connect.Request)
    RequestExpectations.addRequest(req1)
    RequestExpectations.addRequest(req2)

    expect:
    RequestExpectations.requests().size() == 2
  }

  def "requests(matcher) filters by matcher"() {
    given:
    def req1 = mock(com.mx.path.core.common.connect.Request)
    def req2 = mock(com.mx.path.core.common.connect.Request)
    when(req1.getPath()).thenReturn("accounts")
    when(req2.getPath()).thenReturn("transactions")
    RequestExpectations.addRequest(req1)
    RequestExpectations.addRequest(req2)

    when:
    def matched = RequestExpectations.requests(RequestMatcher.Fluent.withPath("accounts"))

    then:
    matched.size() == 1
    matched[0].is(req1)
  }

  def "request(matcher) returns first match"() {
    given:
    def req = mock(com.mx.path.core.common.connect.Request)
    when(req.getPath()).thenReturn("accounts")
    RequestExpectations.addRequest(req)

    expect:
    RequestExpectations.request(RequestMatcher.Fluent.withPath("accounts")).is(req)
    RequestExpectations.request(RequestMatcher.Fluent.withPath("other")) == null
  }

  def "verifyConnectionExpectations passes with no expectations"() {
    expect:
    RequestExpectations.verifyConnectionExpectations()
  }

  def "Fluent.expectRequest registers expectation"() {
    when:
    RequestExpectations.Fluent.expectRequest(RequestMatcher.Fluent.withPath("accounts"))

    then:
    RequestExpectations.getRequestExpectations().size() == 1
  }

  def "Fluent.stubRequest registers allowance"() {
    when:
    RequestExpectations.Fluent.stubRequest(RequestMatcher.Fluent.withPath("accounts"))

    then:
    RequestExpectations.getRequestAllowances().size() == 1
  }

  def "setupConnection"() {
    given:
    def connection = RequestExpectations.Fluent.setupConnection(new TestConnection())

    when:
    def baseFilters = connection.getBaseRequestFilters()
    def connectionFilters = connection.connectionRequestFilters()

    then:
    baseFilters.size() == 3
    baseFilters.collect { it.getClass() } == [
      ErrorHandlerFilter,
      CallbacksFilter,
      RequestFinishedFilter
    ]
    connectionFilters.size() == 1
    connectionFilters.collect { it.getClass() } == [TestRequestFilter]
  }
}
