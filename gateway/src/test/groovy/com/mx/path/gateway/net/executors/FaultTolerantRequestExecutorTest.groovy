package com.mx.path.gateway.net.executors

import static org.mockito.Mockito.any
import static org.mockito.Mockito.doThrow
import static org.mockito.Mockito.eq
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import com.mx.common.exception.request.accessor.connect.ConnectException
import com.mx.common.exception.request.accessor.connect.TimeoutException
import com.mx.common.process.FaultTolerantExecutor
import com.mx.common.process.FaultTolerantTask
import com.mx.common.request.Feature
import com.mx.path.gateway.context.GatewayRequestContext
import com.mx.path.gateway.net.Request
import com.mx.path.gateway.net.Response
import com.mx.path.model.context.RequestContext
import com.mx.path.model.context.facility.Facilities
import com.mx.testing.FaultTolerantExecutorImpl

import spock.lang.Specification

class FaultTolerantRequestExecutorTest extends Specification {
  RequestExecutor nextExecutor
  Request request
  Response response
  FaultTolerantRequestExecutor subject

  def setup() {
    nextExecutor = mock(RequestExecutor.class)
    request = mock(Request.class)
    response = new Response()
    subject = spy(new FaultTolerantRequestExecutor(nextExecutor))
    when(request.getFeatureName()).thenReturn("test")
    when(request.getFeature()).thenReturn(Feature.ACCOUNTS)
    when(request.getRequestTimeOut()).thenReturn(null)
    GatewayRequestContext.builder().clientId("client").feature("test").op("get").build().register()
  }

  def cleanup() {
    Facilities.reset()
    RequestContext.clear()
  }

  def "interacts with request and calls next (no custom scope)"() {
    given:
    def faultTolerantExecutor = spy(new FaultTolerantExecutorImpl(new FaultTolerantExecutorImpl.Config()))
    Facilities.setFaultTolerantExecutor("client", faultTolerantExecutor)

    when:
    subject.execute(request, response)

    then:
    noExceptionThrown()
    verify(faultTolerantExecutor).submit(eq("http.test.get"), any(FaultTolerantTask)) || true
    verify(subject).next(any(Request), any(Response)) || true
  }

  def "interacts with request and calls next (custom scope)"() {
    given:
    when(request.getFaultTolerantScope()).thenReturn("myCustomScope")

    def faultTolerantExecutor = spy(new FaultTolerantExecutorImpl(new FaultTolerantExecutorImpl.Config()))
    Facilities.setFaultTolerantExecutor("client", faultTolerantExecutor)

    when:
    subject.execute(request, response)

    then:
    noExceptionThrown()
    verify(faultTolerantExecutor).submit(eq("myCustomScope"), any(FaultTolerantTask)) || true
    verify(subject).next(any(Request), any(Response)) || true
  }

  def "attaches the causal exception correctly"() {
    given:
    def faultTolerantExecutor = mock(FaultTolerantExecutor)
    Facilities.setFaultTolerantExecutor("client", faultTolerantExecutor)

    when: "an unknown error occurred"
    subject = spy(new FaultTolerantRequestExecutor(nextExecutor))
    doThrow(new RuntimeException("Uh oh!")).when(faultTolerantExecutor).submit(any(String), any(FaultTolerantTask))
    subject.execute(request, response)

    then:
    def e = thrown(ConnectException)
    e.cause.class == RuntimeException
    e.cause.message == "Uh oh!"

    when: "a known error occurred"
    subject = spy(new FaultTolerantRequestExecutor(nextExecutor))
    doThrow(new TimeoutException()).when(faultTolerantExecutor).submit(any(String), any(FaultTolerantTask))
    subject.execute(request, response)

    then:
    e = thrown(TimeoutException)
  }
}