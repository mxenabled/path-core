package com.mx.path.connect.messaging.remote

import com.mx.path.connect.messaging.MessageHeaders
import com.mx.path.connect.messaging.MessageRequest
import com.mx.path.connect.messaging.remote.RemoteTracePropagation
import com.mx.path.core.context.tracing.CustomTracer

import io.opentracing.Scope
import io.opentracing.Span
import io.opentracing.mock.MockTracer

import spock.lang.Specification

class RemoteTracePropagationTest extends Specification {

  MockTracer tracer
  Span span
  Scope scope

  def setup() {
    tracer = new MockTracer()
    span = tracer.buildSpan("rootSpan").start()
    scope = tracer.activateSpan(span)
    CustomTracer.setTracer(tracer)
  }

  def cleanup() {
    scope.close()
    tracer.reset()
    CustomTracer.setTracer(null)
  }

  def "extract"() {
    given:
    def headers = MessageHeaders.builder()
        .header("traceid", "1")
        .header("spanid", "2")
        .build()

    def message = MessageRequest.builder().messageHeaders(headers).channel("channel1").build()

    when:
    Span extracted = RemoteTracePropagation.extract(message)

    then:
    extracted.context().toTraceId() == "1"
    extracted.context().toSpanId() != "2"
  }

  def "inject"() {
    given:
    def message = MessageRequest.builder().messageHeaders(new MessageHeaders()).build()

    when:
    RemoteTracePropagation.inject(message)

    then:
    message.messageHeaders.get("traceid") == tracer.activeSpan().context().toTraceId()
    message.messageHeaders.get("spanid") == tracer.activeSpan().context().toSpanId()
  }
}
