package com.mx.path.core.context.tracing

import com.mx.path.core.context.tracing.HttpHeadersCarrier

import spock.lang.Specification

class HttpHeadersCarrierTest extends Specification {

  def "put adds entry to headers map"() {
    given:
    def headers = [:]
    def carrier = new HttpHeadersCarrier(headers)

    when:
    carrier.put("X-Trace-Id", "abc123")

    then:
    headers["X-Trace-Id"] == "abc123"
  }

  def "iterator throws UnsupportedOperationException"() {
    given:
    def carrier = new HttpHeadersCarrier([:])

    when:
    carrier.iterator()

    then:
    thrown(UnsupportedOperationException)
  }
}
