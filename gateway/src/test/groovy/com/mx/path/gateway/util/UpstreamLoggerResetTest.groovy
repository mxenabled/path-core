package com.mx.path.gateway.util

import static org.mockito.Mockito.mock

import org.slf4j.Logger

import spock.lang.Specification

class UpstreamLoggerResetTest extends Specification {

  def "setLogger and resetLogger work without error"() {
    given:
    def customLogger = mock(Logger)

    when:
    UpstreamLogger.setLogger(customLogger)

    then:
    noExceptionThrown()

    when:
    UpstreamLogger.resetLogger()

    then:
    noExceptionThrown()
  }
}
