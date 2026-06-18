package com.mx.path.testing

import static org.mockito.Mockito.mock

import com.mx.path.testing.request.RequestExpectations
import com.mx.path.testing.request.RequestMatcher
import com.mx.testing.TestConnection

class TestingBaseSpec extends TestingBase {

  def cleanup() {
    RequestExpectations.reset()
  }

  def "withPath creates path matcher"() {
    given:
    def matcher = withPath("accounts")

    expect:
    matcher instanceof RequestMatcher
  }

  def "withMethod creates method matcher"() {
    given:
    def matcher = withMethod("GET")

    expect:
    matcher instanceof RequestMatcher
  }

  def "expectConnection registers an expectation"() {
    when:
    expectConnection(withPath("accounts"))

    then:
    RequestExpectations.getRequestExpectations().size() == 1
  }

  def "stubConnection registers an allowance"() {
    when:
    stubConnection(withPath("accounts"))

    then:
    RequestExpectations.getRequestAllowances().size() == 1
  }

  def "allowConnection registers an allowance"() {
    when:
    allowConnection(withPath("accounts"))

    then:
    RequestExpectations.getRequestAllowances().size() == 1
  }

  def "setupConnectionMocking resets request expectations"() {
    given:
    expectConnection(withPath("accounts"))

    when:
    setupConnectionMocking()

    then:
    RequestExpectations.getRequestExpectations().isEmpty()
  }

  def "setupConnection returns a wrapped connection"() {
    given:
    def connection = setupConnection(new TestConnection())

    expect:
    connection != null
  }

  def "cleanupRequestExpectations verifies and resets"() {
    when:
    cleanupRequestExpectations()

    then:
    noExceptionThrown()
  }

  def "request() returns first captured request"() {
    given:
    def req = mock(com.mx.path.core.common.connect.Request)
    RequestExpectations.addRequest(req)

    expect:
    request() != null
  }

  def "request(matcher) returns matching request"() {
    given:
    def req = mock(com.mx.path.core.common.connect.Request)
    org.mockito.Mockito.when(req.getPath()).thenReturn("accounts")
    RequestExpectations.addRequest(req)

    expect:
    request(withPath("accounts")) != null
    request(withPath("other")) == null
  }

  def "requests() returns all captured requests"() {
    given:
    RequestExpectations.addRequest(mock(com.mx.path.core.common.connect.Request))

    expect:
    requests().size() == 1
  }

  def "exactly creates an exact-match matcher"() {
    given:
    def req = mock(com.mx.path.core.common.connect.Request)

    when:
    def matcher = exactly(req)

    then:
    matcher != null
    matcher.isMatch(req)
  }

  def "requests(matcher) returns filtered requests"() {
    given:
    def req = mock(com.mx.path.core.common.connect.Request)
    org.mockito.Mockito.when(req.getPath()).thenReturn("accounts")
    RequestExpectations.addRequest(req)

    expect:
    requests(withPath("accounts")).size() == 1
    requests(withPath("other")).size() == 0
  }
}
