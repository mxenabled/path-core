package com.mx.path.connect.messaging.remote

import com.mx.path.connect.messaging.MessageHeaders
import com.mx.path.connect.messaging.MessageRequest

import io.opentracing.Scope
import io.opentracing.Span
import io.opentracing.mock.MockTracer
import io.opentracing.util.GlobalTracer

import spock.lang.Specification

class RemoteTracePropagationTest extends Specification {

  MockTracer tracer
  Span span
  Scope scope


  def setup() {
    tracer = new MockTracer()
    span = tracer.buildSpan("rootSpan").start()
    scope = tracer.activateSpan(span)
    GlobalTracer.registerIfAbsent(tracer)
  }

  def cleanup() {
    scope.close()
    tracer.reset()
  }

  def "extract"() {
    given:
    def headers = MessageHeaders.builder()
        .header("traceid", "1")
        .header("spanid", "2")
        .build()

    def message = MessageRequest.builder().messageHeaders(headers).channel("channel1").build()


    def injectMessage = MessageRequest.builder().messageHeaders(new MessageHeaders()).build()

    when:
    Span extracted = RemoteTracePropagation.extract(message)


    RemoteTracePropagation.inject(injectMessage)

    then:
    extracted.context().toTraceId() == "1"
    extracted.context().toSpanId() != "2"


    injectMessage.messageHeaders.get("traceid") == tracer.activeSpan().context().toTraceId()
    injectMessage.messageHeaders.get("spanid") == tracer.activeSpan().context().toSpanId()
  }
}
