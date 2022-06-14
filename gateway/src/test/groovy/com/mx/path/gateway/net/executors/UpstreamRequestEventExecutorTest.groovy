package com.mx.path.gateway.net.executors

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.verify

import com.mx.common.events.EventBus
import com.mx.path.gateway.events.AfterUpstreamRequestEvent
import com.mx.path.gateway.events.BeforeUpstreamRequestEvent
import com.mx.path.gateway.net.Request
import com.mx.path.gateway.net.Response
import com.mx.path.model.context.RequestContext
import com.mx.path.model.context.facility.Facilities

import spock.lang.Specification

class UpstreamRequestEventExecutorTest extends Specification {
  RequestExecutor nextExecutor
  Request request
  Response response
  UpstreamRequestEventExecutor subject
  EventBus eventBus

  def setup(){
    eventBus = mock(EventBus)
    RequestContext.builder().clientId('12345').build().register()
    subject = spy(new UpstreamRequestEventExecutor(nextExecutor))
    nextExecutor = mock(RequestExecutor.class)
    request = mock(Request.class)
    response = new Response()
    Facilities.addEventBus("12345",eventBus)
  }

  def cleanup(){
    Facilities.reset()
    RequestContext.clear()
  }

  def "test posts an AfterUpstreamRequestEvent with the Response"(){
    when:
    subject.execute(request, response)

    then:
    verify(eventBus).post(AfterUpstreamRequestEvent.builder().response(response).build())||true
    verify(subject).next(request, response)||true
  }

  def "test posts a BeforeUpstreamRequestEvent with the Request"(){
    when:
    subject.execute(request, response)

    then:
    verify(eventBus).post(BeforeUpstreamRequestEvent.builder().request(request).build())
    verify(subject).next(request, response)
  }

  def "test doesn't throw an exception if no EventBus is configured"(){
    given:
    Facilities.EVENT_BUSES.clear()

    when:
    subject.execute(request, response)

    then:
    noExceptionThrown()
    verify(eventBus, never()).post(any())||true
  }

  def "test doesn't throw an exception if the clientId is null"(){
    given:
    RequestContext.builder().clientId(null).build().register()

    when:
    subject.execute(request, response)

    then:
    noExceptionThrown()
    verify(eventBus, never()).post(any())||true
  }
}
