package com.mx.path.testing

import com.mx.path.core.common.connect.Request
import com.mx.path.gateway.accessor.AccessorConnectionBase
import com.mx.path.testing.request.RequestExpectation
import com.mx.path.testing.request.RequestExpectations
import com.mx.path.testing.request.RequestMatcher

import spock.lang.Specification

class TestingBase extends Specification {

  def setupSpec() {
    safeInvoke("setupMockery")
    safeInvoke("setupSessionRepository")
  }

  def cleanup() {
    safeInvoke("cleanupRequestExpectations")
    safeInvoke("cleanupMockery")
    safeInvoke("clearSessionRepository")
  }

  RequestExpectation allowConnection(RequestMatcher requestMatcher) {
    stubConnection(requestMatcher)
  }

  def cleanupRequestExpectations() {
    RequestExpectations.verifyConnectionExpectations()
    RequestExpectations.reset()
  }

  RequestMatcher exactly(Request request) {
    RequestMatcher.Fluent.exactly(request)
  }

  RequestExpectation expectConnection(RequestMatcher requestMatcher) {
    RequestExpectations.Fluent.expectRequest(requestMatcher)
  }

  Request request() {
    RequestExpectations.request()
  }

  Request request(RequestMatcher requestMatcher) {
    RequestExpectations.request(requestMatcher)
  }

  List<Request> requests() {
    RequestExpectations.requests()
  }

  List<Request> requests(RequestMatcher requestMatcher) {
    RequestExpectations.requests(requestMatcher)
  }

  def setupConnectionMocking() {
    RequestExpectations.reset()
  }

  AccessorConnectionBase setupConnection(AccessorConnectionBase connection) {
    return RequestExpectations.Fluent.setupConnection(connection)
  }

  RequestExpectation stubConnection(RequestMatcher requestMatcher) {
    RequestExpectations.Fluent.stubRequest(requestMatcher)
  }

  RequestMatcher withMethod(String method) {
    RequestMatcher.Fluent.withMethod(method)
  }

  RequestMatcher withPath(String path) {
    RequestMatcher.Fluent.withPath(path)
  }

  private def safeInvoke(String methodName) {
    if (respondsTo(methodName)) {
      println("Invoking ${methodName}")
      invokeMethod(methodName, [])
    }
  }
}
