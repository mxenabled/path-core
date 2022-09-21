package com.mx.common.exception

import com.mx.common.accessors.PathResponseStatus

import spock.lang.Specification

class PathRequestExceptionTest extends Specification {

  class TestPathRequestException extends PathRequestException {
    public TestPathRequestException() {
      super();
    }

    public TestPathRequestException(String message) {
      super(message);
    }

    public TestPathRequestException(Throwable cause) {
      super(cause);
    }

    public TestPathRequestException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  def "constructors"() {
    when:
    PathRequestException subject = new TestPathRequestException()

    then:
    verifyAll(subject) {
      getMessage() == "Unknown error"
      getCause() == null
      getStatus() == PathResponseStatus.INTERNAL_ERROR
      shouldReport()
    }

    when:
    subject = new TestPathRequestException("Original message")

    then:
    verifyAll(subject) {
      getMessage() == "Original message"
      getCause() == null
      getStatus() == PathResponseStatus.INTERNAL_ERROR
      shouldReport()
    }

    when:
    def cause = new RuntimeException()
    subject = new TestPathRequestException(cause)

    then:
    verifyAll(subject) {
      getMessage() == "Unknown error"
      getCause() == cause
      getStatus() == PathResponseStatus.INTERNAL_ERROR
      shouldReport()
    }

    when:
    cause = new RuntimeException()
    subject = new TestPathRequestException("Original message", cause)

    then:
    verifyAll(subject) {
      getMessage() == "Original message"
      getCause() == cause
      getStatus() == PathResponseStatus.INTERNAL_ERROR
      shouldReport()
    }
  }

  def "fluent setters"() {
    when:
    def subject = new TestPathRequestException();

    then:
    verifyAll(subject) {
      getCode() == null
      getCause() == null
      getErrorTitle() == null
      getHeaders().isEmpty()
      getMessage() == "Unknown error"
      getReason() == null
      shouldReport()
      getStatus() == PathResponseStatus.INTERNAL_ERROR
      getUserMessage() == null
    }

    when:
    with(subject) {
      withCode("1401")
      withErrorTitle("Error title")
      withHeader("header", "value")
      withMessage("New message")
      withReason("Because")
      withReport(false)
      withStatus(PathResponseStatus.UNAVAILABLE)
      withUserMessage("User message")
    }

    then:
    verifyAll(subject) {
      getCode() == "1401"
      getCause() == null
      getErrorTitle() == "Error title"
      getHeaders().get("header") == "value"
      getMessage() == "New message"
      getReason() == "Because"
      !shouldReport()
      getStatus() == PathResponseStatus.UNAVAILABLE
      getUserMessage() == "User message"
    }
  }
}
