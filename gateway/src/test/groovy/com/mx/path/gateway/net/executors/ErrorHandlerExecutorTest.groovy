package com.mx.path.gateway.net.executors

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import com.mx.common.exception.request.accessor.connect.TimeoutException
import com.mx.path.gateway.net.Request
import com.mx.path.gateway.net.Response
import com.mx.path.gateway.util.UpstreamLogger

import spock.lang.Specification


class ErrorHandlerExecutorTest extends Specification {
  RequestExecutor nextExecutor
  Request request
  Response response
  ErrorHandlerExecutor subject
  UpstreamLogger upstreamLogger

  def setup() {
    upstreamLogger = mock(UpstreamLogger.class)
    ErrorHandlerExecutor.setUpstreamLogger(upstreamLogger)
  }

  def cleanup() {
    ErrorHandlerExecutor.resetUpstreamLogger()
  }

  def "thrown exception sets response exception"() {
    given:
    nextExecutor = mock(RequestExecutor.class)
    request = mock(Request.class)
    response = new Response()
    subject = new ErrorHandlerExecutor(nextExecutor)
    when(nextExecutor.execute(request, response)).thenThrow(new RuntimeException())

    when:
    subject.execute(request, response)

    then:
    response.getException().class == RuntimeException
  }

  def "ConnectException thrown exception bubbles up"() {
    given:
    nextExecutor = mock(RequestExecutor.class)
    request = mock(Request.class)
    response = new Response()
    subject = new ErrorHandlerExecutor(nextExecutor)
    when(nextExecutor.execute(request, response)).thenThrow(new TimeoutException("bad robot!", new RuntimeException()))

    when:
    subject.execute(request, response)
    verify(upstreamLogger.logRequest(response))

    then:
    thrown(TimeoutException)
  }
}
