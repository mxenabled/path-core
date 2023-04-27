package com.mx.path.gateway.util

import com.mx.path.core.common.accessor.BadRequestException
import com.mx.path.core.common.accessor.RequestValidationException

import spock.lang.Specification

class LoggingExceptionFormatterTest extends Specification {
  def "formatLoggingException()"() {
    given:
    def exception = new BadRequestException("Some failure occurred!", "User Message!");

    when:
    def result = LoggingExceptionFormatter.formatLoggingException(exception)

    then:
    result == "com.mx.path.core.common.accessor.BadRequestException: Some failure occurred!"

    when:
    result = LoggingExceptionFormatter.formatLoggingException(new RequestValidationException("Another failure!", "Another user message", new Exception("Something broke internally")))

    then:
    result == "com.mx.path.core.common.accessor.RequestValidationException: Another failure!\n\tCaused by: java.lang.Exception: Something broke internally"
  }

  def "formatLoggingExceptionWithStacktrace()"() {
    given:
    def exception = new BadRequestException("Some failure occurred!", "User Message!");

    when:
    def result = LoggingExceptionFormatter.formatLoggingExceptionWithStacktrace(exception)

    then:
    true
    result != null

    when:
    result = LoggingExceptionFormatter.formatLoggingExceptionWithStacktrace(new RequestValidationException("Another failure!", "Another user message", new Exception("Something broke internally")))

    then:
    result != null
  }
}
