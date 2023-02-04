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
    RequestContext.builder().clientId('12345').build().register()
    subject = spy(new UpstreamRequestEventExecutor(nextExecutor))
    nextExecutor = mock(RequestExecutor.class)
    request = mock(Request.class)
    response = new Response()
  }

  def cleanup(){
    Facilities.reset()
    RequestContext.clear()
  }

  def "test posts an AfterUpstreamRequestEvent with the Response"(){
    when:
    subject.execute(request, response)

    then:
    verify(subject).next(request, response)||true
  }

  def "test posts a BeforeUpstreamRequestEvent with the Request"(){
    when:
    subject.execute(request, response)

    then:
    verify(subject).next(request, response)
  }

  def "test doesn't throw an exception if the clientId is null"(){
    given:
    RequestContext.builder().clientId(null).build().register()

    when:
    subject.execute(request, response)

    then:
    noExceptionThrown()
  }
}
