package com.mx.path.gateway.net.executors

import static org.mockito.Mockito.any
import static org.mockito.Mockito.doAnswer
import static org.mockito.Mockito.doThrow
import static org.mockito.Mockito.eq
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import com.mx.common.accessors.PathResponseStatus
import com.mx.common.http.HttpStatus
import com.mx.common.process.FaultTolerantExecutionException
import com.mx.common.process.FaultTolerantExecutor
import com.mx.common.process.FaultTolerantTask
import com.mx.common.request.Feature
import com.mx.path.gateway.context.GatewayRequestContext
import com.mx.path.gateway.net.HystrixConfigurations
import com.mx.path.gateway.net.Request
import com.mx.path.gateway.net.Response
import com.mx.path.gateway.net.UpstreamConnectionException
import com.mx.path.gateway.util.MdxApiException
import com.mx.path.model.context.RequestContext
import com.mx.path.model.context.facility.Facilities
import com.mx.testing.FaultTolerantExecutorImpl
import com.netflix.hystrix.exception.HystrixRuntimeException

import spock.lang.Specification
import spock.lang.Unroll

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

  @Unroll
  def "translates FaultTolerantExecutionExceptions to MdxApiExceptions"() {
    given:
    def faultTolerantExecutor = mock(FaultTolerantExecutor)
    Facilities.setFaultTolerantExecutor("client", faultTolerantExecutor)

    doThrow(exception).when(faultTolerantExecutor).submit(any(String), any(FaultTolerantTask))

    when:
    subject.execute(request, response)

    then:
    def e = thrown(MdxApiException)
    e.status == status
    response.status == status

    where:
    exception                                                                          || status
    new FaultTolerantExecutionException(PathResponseStatus.INTERNAL_ERROR)             || HttpStatus.INTERNAL_SERVER_ERROR
    new FaultTolerantExecutionException(PathResponseStatus.TOO_MANY_REQUESTS)          || HttpStatus.TOO_MANY_REQUESTS
    new FaultTolerantExecutionException(PathResponseStatus.UNAVAILABLE)                || HttpStatus.SERVICE_UNAVAILABLE
    new FaultTolerantExecutionException(PathResponseStatus.TIMEOUT)                    || HttpStatus.GATEWAY_TIMEOUT
    new FaultTolerantExecutionException("Uh oh.",
        new UpstreamConnectionException(new SocketTimeoutException()), PathResponseStatus.INTERNAL_ERROR) || HttpStatus.GATEWAY_TIMEOUT
    new FaultTolerantExecutionException("Uh oh.",
        new MdxApiException(HttpStatus.BAD_REQUEST), PathResponseStatus.INTERNAL_ERROR)                   || HttpStatus.BAD_REQUEST
    new RuntimeException("Uh oh.")                                                     || HttpStatus.INTERNAL_SERVER_ERROR
  }

  def "attaches the causal exception correctly"() {
    given:
    def faultTolerantExecutor = mock(FaultTolerantExecutor)
    Facilities.setFaultTolerantExecutor("client", faultTolerantExecutor)


    when: "an unknown error occurred"
    subject = spy(new FaultTolerantRequestExecutor(nextExecutor))
    doThrow(new FaultTolerantExecutionException(new RuntimeException("Uh oh!"), PathResponseStatus.INTERNAL_ERROR)).when(faultTolerantExecutor).submit(any(String), any(FaultTolerantTask))
    subject.execute(request, response)

    then:
    def e = thrown(MdxApiException)
    e.cause.class == RuntimeException
    e.cause.message == "Uh oh!"

    when: "a known error occurred"
    subject = spy(new FaultTolerantRequestExecutor(nextExecutor))
    doThrow(new FaultTolerantExecutionException(PathResponseStatus.TIMEOUT)).when(faultTolerantExecutor).submit(any(String), any(FaultTolerantTask))
    subject.execute(request, response)

    then:
    e = thrown(MdxApiException)
    e.cause.class == FaultTolerantExecutionException
    e.cause.status == PathResponseStatus.TIMEOUT
  }

  def "interacts with request and calls next (Hystrix fallback)"() {
    given:
    new HystrixConfigurations().tap {
      setEnableHystrixWrapper(true)
      setEnableCircuitBreaker(true)
      setTimeoutInMs(1000)
      setGroupName("unittest")
    }

    // Force a timeout
    doAnswer({ Thread.sleep(10000) }).when(nextExecutor).execute(request, response)

    when:
    subject.execute(request, response)

    then:
    thrown(HystrixRuntimeException)
    verify(subject).next(request, response) || true
  }

  def "interacts with request and calls next (unprotected fallback)"() {
    given:
    new HystrixConfigurations().tap { setEnableHystrixWrapper(false) }

    when:
    subject.execute(request, response)

    then:
    noExceptionThrown()
    verify(subject).next(request, response) || true
    verify(subject, never()).handleHystrixCircuitBreaker(request, response) || true
  }
}