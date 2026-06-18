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

  def "getSpanContext returns null when tracer is null"() {
    given:
    CustomTracer.setTracer(null)

    expect:
    CustomTracer.getSpanContext() == null
  }

  def "startSpan returns span when tracer is set"() {
    given:
    def builder = mock(Tracer.SpanBuilder)
    when(tracer.buildSpan("op")).thenReturn(builder)
    when(builder.start()).thenReturn(span)

    when:
    def result = CustomTracer.startSpan("op")

    then:
    result.is(span)
  }

  def "startSpan returns null when tracer is null"() {
    given:
    CustomTracer.setTracer(null)

    expect:
    CustomTracer.startSpan("op") == null
  }

  def "startChildSpanFromSpanContext builds child span"() {
    given:
    def builder = mock(Tracer.SpanBuilder)
    when(tracer.buildSpan("child")).thenReturn(builder)
    when(builder.asChildOf(context)).thenReturn(builder)
    when(builder.start()).thenReturn(span)

    when:
    def result = CustomTracer.startChildSpanFromSpanContext("child", context)

    then:
    result.is(span)
  }

  def "activateSpan delegates to tracer"() {
    given:
    def scope = mock(io.opentracing.Scope)
    when(tracer.activateSpan(span)).thenReturn(scope)

    when:
    def result = CustomTracer.activateSpan(span)

    then:
    result.is(scope)
  }

  def "injectSpanContextIntoHeaders injects when span context present"() {
    given:
    def headers = [:]
    when(span.setTag(io.opentracing.tag.Tags.SPAN_KIND, io.opentracing.tag.Tags.SPAN_KIND_CLIENT)).thenReturn(span)
    when(span.context()).thenReturn(context)

    when:
    CustomTracer.injectSpanContextIntoHeaders(headers)

    then:
    noExceptionThrown()
  }

  def "injectSpanContextIntoHeaders skips when no span context"() {
    given:
    when(tracer.activeSpan()).thenReturn(null)

    when:
    CustomTracer.injectSpanContextIntoHeaders([:])

    then:
    noExceptionThrown()
  }

  def cleanup() {
    CustomTracer.setTracer(null)
  }
}
