package com.mx.path.gateway.net.executors

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.*

import com.mx.path.gateway.net.Request
import com.mx.path.gateway.net.Response

import spock.lang.Specification

class CallbacksExecutorTest extends Specification {
  RequestExecutor nextExecutor
  Request request
  Response response
  CallbacksExecutor subject

  def "interacts with request and next"() {
    given:
    nextExecutor = mock(RequestExecutor.class)
    request = mock(Request.class)
    response = new Response()
    subject = new CallbacksExecutor(nextExecutor)
    when(request.process(response)).thenReturn("results")

    when:
    subject.execute(request, response)
    verify(nextExecutor).execute(request, response)
    verify(request).completed(any(Response.class))

    then:
    response.getObject(String.class) == "results"
  }
}
