package com.mx.path.gateway.connect.filters

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import com.mx.common.connect.Request
import com.mx.common.connect.RequestFilter
import com.mx.common.connect.Response
import com.mx.common.connect.TimeoutException
import com.mx.path.gateway.util.UpstreamLogger

import spock.lang.Specification

class ErrorHandlerFilterTest extends Specification {
  class TestRequest extends Request<TestRequest, TestResponse> {
    @Override
    TestResponse execute() {
      return null
    }
  }
  class TestResponse extends Response<TestRequest, TestResponse> {
  }

  RequestFilter nextExecutor
  Request request
  Response response
  ErrorHandlerFilter subject
  UpstreamLogger upstreamLogger

  def setup() {
    upstreamLogger = mock(UpstreamLogger.class)
    ErrorHandlerFilter.setUpstreamLogger(upstreamLogger)
  }

  def cleanup() {
    ErrorHandlerFilter.resetUpstreamLogger()
  }

  def "thrown exception sets response exception"() {
    given:
    nextExecutor = mock(RequestFilter.class)
    request = mock(Request.class)
    response = new TestResponse()
    subject = new ErrorHandlerFilter()
    subject.setNext(nextExecutor)
    when(nextExecutor.execute(request, response)).thenThrow(new RuntimeException())

    when:
    subject.execute(request, response)

    then:
    response.getException().class == RuntimeException
  }

  def "ConnectException thrown exception bubbles up"() {
    given:
    nextExecutor = mock(RequestFilter.class)
    request = mock(Request.class)
    response = new TestResponse()
    subject = new ErrorHandlerFilter()
    subject.setNext(nextExecutor)
    when(nextExecutor.execute(request, response)).thenThrow(new TimeoutException("bad robot!", new RuntimeException()))

    when:
    subject.execute(request, response)
    verify(upstreamLogger.logRequest(response))

    then:
    thrown(TimeoutException)
  }
}
