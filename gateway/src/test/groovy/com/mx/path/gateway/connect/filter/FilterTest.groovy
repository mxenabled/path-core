package com.mx.path.gateway.connect.filter

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

import com.mx.path.core.common.accessor.PathResponseStatus
import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.common.connect.Request
import com.mx.path.core.common.connect.RequestFilterBase
import com.mx.path.core.common.connect.Response
import com.mx.path.core.context.RequestContext
import com.mx.path.core.context.facility.Facilities
import com.mx.testing.FaultTolerantExecutorImpl

import spock.lang.Specification

class FilterTest extends Specification {

  // Terminal no-op next filter
  static class NoOpFilter extends RequestFilterBase {
    @Override
    void execute(Request request, Response response) {
    }
  }

  def "HttpClientConnectException sets status and report=true"() {
    given:
    def cause = new RuntimeException("timeout")
    def ex = new HttpClientConnectException("connection timeout", cause)

    expect:
    ex.getMessage() == "connection timeout"
    ex.getCause().is(cause)
    ex.getStatus() == PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE
    ex.shouldReport()
  }

  def "CallbacksFilter invokes next and fires callbacks"() {
    given:
    def request = mock(Request)
    def response = mock(Response)
    def processResult = new Object()

    when(request.process(response)).thenReturn(processResult)

    def filter = new CallbacksFilter()
    def noOp = new NoOpFilter()
    filter.setNext(noOp)

    when:
    filter.execute(request, response)

    then:
    verify(request).completed(response) || true
    verify(request).process(response) || true
    verify(response).withObject(processResult) || true
  }

  def "RequestFinishedFilter always calls response.finish in finally"() {
    given:
    def request = mock(Request)
    def response = mock(Response)

    def filter = new RequestFinishedFilter()
    def noOp = new NoOpFilter()
    filter.setNext(noOp)

    when:
    filter.execute(request, response)

    then:
    verify(response).finish() || true
  }

  def "UpstreamRequestEventFilter skips events when no RequestContext"() {
    given:
    def request = mock(Request)
    def response = mock(Response)
    def filter = new UpstreamRequestEventFilter()
    filter.setNext(new NoOpFilter())
    RequestContext.clear()

    when:
    filter.execute(request, response)

    then:
    noExceptionThrown()
  }

  def "UpstreamRequestEventFilter skips events when no EventBus configured"() {
    given:
    def request = mock(Request)
    def response = mock(Response)
    def filter = new UpstreamRequestEventFilter()
    filter.setNext(new NoOpFilter())
    RequestContext.builder().clientId("client1").build().register()

    when:
    filter.execute(request, response)

    then:
    noExceptionThrown()

    cleanup:
    RequestContext.clear()
    Facilities.reset()
  }

  def "UpstreamRequestEventFilter posts before and after events when EventBus present"() {
    given:
    def request = mock(Request)
    def response = mock(Response)
    def postedEvents = []
    def eventBus = new com.mx.path.core.common.event.EventBus() {
          ObjectMap configurations = new ObjectMap()
          void post(Object event) {
            postedEvents << event
          }
          void register(Object subscriber) {}
        }
    RequestContext.builder().clientId("client1").build().register()
    Facilities.addEventBus("client1", eventBus)
    def filter = new UpstreamRequestEventFilter()
    filter.setNext(new NoOpFilter())

    when:
    filter.execute(request, response)

    then:
    postedEvents.size() == 2

    cleanup:
    RequestContext.clear()
    Facilities.reset()
  }

  def "FaultTolerantRequestFilter delegates to next when no executor configured"() {
    given:
    def request = mock(Request)
    def response = mock(Response)
    when(request.getPath()).thenReturn("accounts")
    RequestContext.builder().clientId("client1").build().register()
    // no FaultTolerantExecutor set in Facilities → else branch
    def filter = new FaultTolerantRequestFilter()
    filter.setNext(new NoOpFilter())

    when:
    filter.execute(request, response)

    then:
    noExceptionThrown()

    cleanup:
    RequestContext.clear()
    Facilities.reset()
  }

  def "FaultTolerantRequestFilter delegates through FTE when configured"() {
    given:
    def nextCalled = false
    def request = mock(Request)
    def response = mock(Response)
    when(request.getRequestTimeOut()).thenReturn(null)
    when(request.getFaultTolerantScope()).thenReturn(null)
    RequestContext.builder().clientId("client1").feature("accounts").build().register()
    def fte = new FaultTolerantExecutorImpl(new FaultTolerantExecutorImpl.Config())
    Facilities.setFaultTolerantExecutor("client1", fte)
    def filter = new FaultTolerantRequestFilter()
    filter.setNext(new RequestFilterBase() {
          void execute(Request req, Response resp) {
            nextCalled = true
          }
        })

    when:
    filter.execute(request, response)

    then:
    nextCalled

    cleanup:
    RequestContext.clear()
    Facilities.reset()
  }

  def "FaultTolerantRequestFilter.buildScope includes feature when set"() {
    given:
    RequestContext.builder().clientId("client1").feature("accounts").build().register()
    def filter = new FaultTolerantRequestFilter()

    when:
    def scope = filter.buildScope()

    then:
    scope.contains("accounts")

    cleanup:
    RequestContext.clear()
  }

  def "FaultTolerantRequestFilter.buildScope returns just http when no feature or op"() {
    given:
    RequestContext.builder().clientId("client1").build().register()
    def filter = new FaultTolerantRequestFilter()

    when:
    def scope = filter.buildScope()

    then:
    scope == "http"

    cleanup:
    RequestContext.clear()
  }

  def "TracingFilter delegates to next when no tracer registered"() {
    given:
    def request = mock(Request)
    def response = mock(Response)
    // GlobalTracer returns a NoopTracer by default; resetting via reflection if needed
    def filter = new TracingFilter()
    filter.setNext(new NoOpFilter())

    when:
    filter.execute(request, response)

    then:
    noExceptionThrown()
  }

  def "RequestFinishedFilter calls finish even when next throws"() {
    given:
    def request = mock(Request)
    def response = mock(Response)

    def throwingFilter = new RequestFilterBase() {
          void execute(Request req, Response resp) {
            throw new RuntimeException("unexpected error")
          }
        }
    def filter = new RequestFinishedFilter()
    filter.setNext(throwingFilter)

    when:
    filter.execute(request, response)

    then:
    thrown(RuntimeException)
    verify(response).finish() || true
  }
}
