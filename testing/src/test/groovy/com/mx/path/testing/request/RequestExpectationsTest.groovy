package com.mx.path.testing.request

import com.mx.path.gateway.connect.filters.CallbacksFilter
import com.mx.path.gateway.connect.filters.ErrorHandlerFilter
import com.mx.path.gateway.connect.filters.RequestFinishedFilter
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
