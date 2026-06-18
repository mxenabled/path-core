package com.mx.path.core.common.connect

import com.mx.path.core.common.accessor.PathResponseStatus

import spock.lang.Specification

class ConnectExceptionsTest extends Specification {

  def "ConnectException with message and cause"() {
    given:
    def cause = new RuntimeException("root")
    def ex = new ConnectException("connect failed", cause)

    expect:
    ex.getMessage() == "connect failed"
    ex.getCause().is(cause)
  }

  def "ConnectException with message and status"() {
    given:
    def ex = new ConnectException("connect failed", PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE)

    expect:
    ex.getMessage() == "connect failed"
    ex.getStatus() == PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE
    !ex.shouldReport()
  }

  def "ConnectException with message, status and cause"() {
    given:
    def cause = new RuntimeException("root")
    def ex = new ConnectException("connect failed", PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE, cause)

    expect:
    ex.getCause().is(cause)
    ex.getStatus() == PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE
  }

  def "TimeoutException default constructor"() {
    given:
    def ex = new TimeoutException()

    expect:
    ex.getMessage() == "A request timeout occurred"
    !ex.shouldReport()
  }

  def "TimeoutException with message"() {
    given:
    def ex = new TimeoutException("timed out after 30s")

    expect:
    ex.getMessage() == "timed out after 30s"
    !ex.shouldReport()
  }

  def "TimeoutException with message and cause"() {
    given:
    def cause = new RuntimeException("socket timeout")
    def ex = new TimeoutException("timed out", cause)

    expect:
    ex.getCause().is(cause)
    !ex.shouldReport()
  }

  def "ServiceUnavailableException with message and cause"() {
    given:
    def cause = new RuntimeException("connection refused")
    def ex = new ServiceUnavailableException("service down", cause)

    expect:
    ex.getMessage() == "service down"
    ex.getCause().is(cause)
    ex.getStatus() == PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE
    !ex.shouldReport()
  }

  def "CircuitOpenException with message and cause"() {
    given:
    def cause = new RuntimeException("circuit open")
    def ex = new CircuitOpenException("circuit is open", cause)

    expect:
    ex.getMessage() == "circuit is open"
    ex.getCause().is(cause)
    ex.isInternal()
  }

  def "MisConfiguredFilterChainException default constructor"() {
    given:
    def ex = new MisConfiguredFilterChainException()

    expect:
    ex.getMessage().contains("filterChain is null")
    ex.shouldReport()
  }

  def "TooManyRequestsException with message and cause"() {
    given:
    def cause = new RuntimeException("rate limited")
    def ex = new TooManyRequestsException("too many requests", cause)

    expect:
    ex.getMessage() == "too many requests"
    ex.getCause().is(cause)
    ex.isInternal()
    !ex.shouldReport()
    ex.getStatus() == PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE
  }
}
