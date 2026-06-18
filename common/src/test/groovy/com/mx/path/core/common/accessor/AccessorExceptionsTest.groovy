package com.mx.path.core.common.accessor

import spock.lang.Specification

class AccessorExceptionsTest extends Specification {

  static class ConcreteSystemException extends AccessorSystemException {
    ConcreteSystemException(String message) {
      super(message)
    }
    ConcreteSystemException(String message, Throwable cause) {
      super(message, cause)
    }
  }

  static class ConcreteUserException extends AccessorUserException {
    ConcreteUserException(String message, PathResponseStatus status) {
      super(message, status)
    }
    ConcreteUserException(String message, PathResponseStatus status, Throwable cause) {
      super(message, status, cause)
    }
    ConcreteUserException(String message, String userMessage, PathResponseStatus status) {
      super(message, userMessage, status)
    }
    ConcreteUserException(String message, String userMessage, PathResponseStatus status, Throwable cause) {
      super(message, userMessage, status, cause)
    }
  }

  def "AccessorSystemException sets INTERNAL_ERROR status and shouldReport=true"() {
    given:
    def ex = new ConcreteSystemException("system error")

    expect:
    ex.getMessage() == "system error"
    ex.getStatus() == PathResponseStatus.INTERNAL_ERROR
    ex.shouldReport()
  }

  def "AccessorSystemException with cause preserves cause"() {
    given:
    def cause = new RuntimeException("root")
    def ex = new ConcreteSystemException("system error", cause)

    expect:
    ex.getCause().is(cause)
    ex.getStatus() == PathResponseStatus.INTERNAL_ERROR
    ex.shouldReport()
  }

  def "AccessorUserException sets status and shouldReport=false"() {
    given:
    def ex = new ConcreteUserException("user error", PathResponseStatus.NOT_FOUND)

    expect:
    ex.getMessage() == "user error"
    ex.getStatus() == PathResponseStatus.NOT_FOUND
    !ex.shouldReport()
  }

  def "AccessorUserException with cause"() {
    given:
    def cause = new RuntimeException("root")
    def ex = new ConcreteUserException("user error", PathResponseStatus.BAD_REQUEST, cause)

    expect:
    ex.getCause().is(cause)
    !ex.shouldReport()
  }

  def "AccessorUserException with userMessage"() {
    given:
    def ex = new ConcreteUserException("internal msg", "user msg", PathResponseStatus.UNAUTHORIZED)

    expect:
    ex.getMessage() == "internal msg"
    ex.getUserMessage() == "user msg"
    ex.getStatus() == PathResponseStatus.UNAUTHORIZED
  }

  def "AccessorUserException with userMessage and cause"() {
    given:
    def cause = new RuntimeException("root")
    def ex = new ConcreteUserException("internal msg", "user msg", PathResponseStatus.UNAUTHORIZED, cause)

    expect:
    ex.getCause().is(cause)
    ex.getUserMessage() == "user msg"
  }

  def "ResourceNotFoundException message constructor"() {
    given:
    def ex = new ResourceNotFoundException("not found")

    expect:
    ex.getStatus() == PathResponseStatus.NOT_FOUND
    ex.getMessage() == "not found"
  }

  def "ResourceNotFoundException message+cause constructor"() {
    given:
    def cause = new RuntimeException("root")
    def ex = new ResourceNotFoundException("not found", cause)

    expect:
    ex.getCause().is(cause)
    ex.getStatus() == PathResponseStatus.NOT_FOUND
  }

  def "UnauthorizedException constructors"() {
    given:
    def ex = new UnauthorizedException("unauthorized", "please log in")

    expect:
    ex.getStatus() == PathResponseStatus.UNAUTHORIZED
    ex.getUserMessage() == "please log in"
  }

  def "UnauthorizedException with cause"() {
    given:
    def cause = new RuntimeException("root")
    def ex = new UnauthorizedException("unauthorized", "please log in", cause)

    expect:
    ex.getCause().is(cause)
    ex.getStatus() == PathResponseStatus.UNAUTHORIZED
  }

  def "BadRequestException sets BAD_REQUEST status"() {
    given:
    def ex = new BadRequestException("bad input")

    expect:
    ex.getStatus() == PathResponseStatus.BAD_REQUEST
  }

  def "UpstreamResponseError single-arg constructor"() {
    given:
    def ex = new UpstreamResponseError("upstream error")

    expect:
    ex.getMessage() == "upstream error"
    ex.shouldReport()
  }

  def "UpstreamResponseError with cause"() {
    given:
    def cause = new RuntimeException("root")
    def ex = new UpstreamResponseError("upstream error", cause)

    expect:
    ex.getCause().is(cause)
  }

  def "RequestValidationException with message"() {
    given:
    def ex = new RequestValidationException("validation failed")

    expect:
    ex.getMessage() == "validation failed"
    ex.getStatus() == PathResponseStatus.USER_ERROR
  }

  def "RequestValidationException with message and userMessage"() {
    given:
    def ex = new RequestValidationException("validation failed", "please fix input")

    expect:
    ex.getUserMessage() == "please fix input"
  }

  def "RequestPayloadException sets shouldReport=true and isInternal=true"() {
    given:
    def ex = new RequestPayloadException("payload error")

    expect:
    ex.getMessage() == "payload error"
    ex.shouldReport()
    ex.isInternal()
  }

  def "ResponsePayloadException sets shouldReport=true and isInternal=true"() {
    given:
    def ex = new ResponsePayloadException("response error")

    expect:
    ex.getMessage() == "response error"
    ex.shouldReport()
    ex.isInternal()
  }

  def "UpstreamResponseProcessingException constructors"() {
    given:
    def cause = new RuntimeException("root")

    expect:
    new UpstreamResponseProcessingException("processing error").getMessage() == "processing error"
    new UpstreamResponseProcessingException("processing error", cause).getCause().is(cause)
  }

  def "UpstreamResponseValidationException constructors"() {
    given:
    def cause = new RuntimeException("root")

    expect:
    new UpstreamResponseValidationException("validation error").getMessage() == "validation error"
    new UpstreamResponseValidationException("validation error", cause).getCause().is(cause)
  }

  def "UpstreamSystemUnavailable constructors"() {
    given:
    def cause = new RuntimeException("root")

    expect:
    new UpstreamSystemUnavailable("unavailable").getMessage() == "unavailable"
    new UpstreamSystemUnavailable("unavailable", cause).getCause().is(cause)
    new UpstreamSystemUnavailable("unavailable", "user msg").getUserMessage() == "user msg"
    new UpstreamSystemUnavailable("unavailable", "user msg", cause).getCause().is(cause)
  }

  def "UpstreamSystemMaintenance sets errorTitle"() {
    given:
    def ex = new UpstreamSystemMaintenance("maintenance")

    expect:
    ex.getMessage() == "maintenance"
    ex.getErrorTitle() == "System under maintenance"
  }

  def "UpstreamSystemMaintenance with cause"() {
    given:
    def cause = new RuntimeException("root")
    def ex = new UpstreamSystemMaintenance("maintenance", cause)

    expect:
    ex.getCause().is(cause)
    ex.getErrorTitle() == "System under maintenance"
  }
}
