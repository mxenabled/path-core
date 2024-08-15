package com.mx.path.core.common.exception

import com.mx.path.core.common.accessor.PathResponseStatus

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
      !isInternal()
      getStatus() == PathResponseStatus.INTERNAL_ERROR
      shouldReport()
    }

    when:
    subject = new TestPathRequestException("Original message")

    then:
    verifyAll(subject) {
      getMessage() == "Original message"
      getCause() == null
      !isInternal()
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
      !isInternal()
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
      !isInternal()
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
      !isInternal()
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
      withIsInternal(true)
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
      getHeaders().get("X-Internal-Error") == "true"
      isInternal()
      getMessage() == "New message"
      getReason() == "Because"
      !shouldReport()
      getStatus() == PathResponseStatus.UNAVAILABLE
      getUserMessage() == "User message"
    }
  }
}
