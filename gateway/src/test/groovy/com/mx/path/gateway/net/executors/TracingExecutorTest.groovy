package com.mx.path.gateway.net.executors

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import com.mx.path.gateway.net.Request
import com.mx.path.gateway.net.Response
import com.mx.path.model.context.tracing.CustomTracer

import io.opentracing.mock.MockTracer

import spock.lang.Specification

class TracingExecutorTest extends Specification {
  RequestExecutor nextExecutor
  Request request
  Response response
  TracingExecutor subject
  MockTracer tracer

  def setup() {
    tracer = new MockTracer()
    CustomTracer.setTracer(tracer)
  }

  def "interacts with request and next"() {
    given:
    nextExecutor = mock(RequestExecutor.class)
    request = new Request()
    response = new Response()
    subject = new TracingExecutor(nextExecutor)

    when:

    subject.execute(request, response)
    verify(nextExecutor).execute(request, response)

    then:
    tracer.finishedSpans().size() == 1
    request.getTraceId() == "1"
    request.getTraceSpanId() == "2"
  }
}
