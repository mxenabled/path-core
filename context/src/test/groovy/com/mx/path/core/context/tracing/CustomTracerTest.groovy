package com.mx.path.core.context.tracing

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

import io.opentracing.Span
import io.opentracing.SpanContext
import io.opentracing.Tracer

import spock.lang.Specification

class CustomTracerTest extends Specification {
  Tracer tracer
  Span span
  SpanContext context

  def setup() {
    tracer = mock(Tracer.class)
    span = mock(Span.class)
    context = mock(SpanContext.class)
    when(tracer.activeSpan()).thenReturn(span)
    when(span.context()).thenReturn(context)

    CustomTracer.setTracer(tracer)
  }

  def "setSpanSetsSingleton"() {
    given:
    tracer = mock(Tracer.class)
    CustomTracer.setTracer(tracer)

    expect:
    tracer.is(CustomTracer.getTracer())
  }

  def "shouldGetCurrentSpan"() {
    when:
    when(context.toSpanId()).thenReturn("span1")

    then:
    "span1" == CustomTracer.getSpanId()
  }

  def "shouldGetCurrentTraceId"() {
    when:
    when(context.toTraceId()).thenReturn("trace1")

    then:
    "trace1" == CustomTracer.getTraceId()
  }

  def "when no currentSpan"() {
    when:
    when(tracer.activeSpan()).thenReturn(null)

    then:
    null == CustomTracer.getTraceId()
    null == CustomTracer.getSpanId()
  }
}
