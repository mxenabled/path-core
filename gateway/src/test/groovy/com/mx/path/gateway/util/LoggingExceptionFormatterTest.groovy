package com.mx.path.gateway.util

import com.mx.common.http.HttpStatus

import spock.lang.Specification

class LoggingExceptionFormatterTest extends Specification {
  def "formatLoggingException()"() {
    given:
    def exception = new MdxApiException("Some failure occurred!", HttpStatus.BAD_REQUEST, false, null);

    when:
    def result = LoggingExceptionFormatter.formatLoggingException(exception)

    then:
    result == "com.mx.path.gateway.util.MdxApiException: Some failure occurred!"

    when:
    result = LoggingExceptionFormatter.formatLoggingException(new MdxApiException("Another failure!", HttpStatus.UNPROCESSABLE_ENTITY, false, new Exception("Something broke internally")))

    then:
    result == "com.mx.path.gateway.util.MdxApiException: Another failure!\n\tCaused by: java.lang.Exception: Something broke internally"
  }

  def "formatLoggingExceptionWithStacktrace()"() {
    given:
    def exception = new MdxApiException("Some failure occurred!", HttpStatus.BAD_REQUEST, false, null);

    when:
    def result = LoggingExceptionFormatter.formatLoggingExceptionWithStacktrace(exception)

    then:
    true
    result != null

    when:
    result = LoggingExceptionFormatter.formatLoggingExceptionWithStacktrace(new MdxApiException("Another failure!", HttpStatus.UNPROCESSABLE_ENTITY, false, new Exception("Something broke internally")))

    then:
    result != null
  }
}
