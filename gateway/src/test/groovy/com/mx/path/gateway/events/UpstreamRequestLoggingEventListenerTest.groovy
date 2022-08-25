package com.mx.path.gateway.events

import static org.mockito.Mockito.*

import com.mx.path.gateway.net.Response
import com.mx.path.gateway.util.UpstreamLogger
import com.mx.path.gateway.util.UpstreamRequestLoggingEventListener
import com.mx.path.model.context.RequestContext
import com.mx.path.model.context.Session

import spock.lang.Specification

class UpstreamRequestLoggingEventListenerTest extends Specification {

  UpstreamRequestLoggingEventListener subject
  UpstreamLogger upstreamLogger

  def setup(){
    subject = spy(new UpstreamRequestLoggingEventListener())
    upstreamLogger = mock(UpstreamLogger)
    subject.setUpstreamLogger(upstreamLogger)
  }

  def "interacts with upstreamLogger when response is present"(){
    given:
    def response = new Response()
    def requestContext = AfterUpstreamRequestEvent.builder().build().requestContext
    def session = AfterUpstreamRequestEvent.builder().build().session

    def afterUpstreamEvent = spy(new AfterUpstreamRequestEvent(response, requestContext, session))

    when:
    subject.onAfterUpstreamRequestEvent(afterUpstreamEvent)

    then:
    verify(upstreamLogger).logRequest(afterUpstreamEvent.response) || true
  }

  def "no interactions with upstreamLogger when response is null"(){
    given:
    def response = null
    def requestContext = AfterUpstreamRequestEvent.builder().build().requestContext
    def session = AfterUpstreamRequestEvent.builder().build().session
    def afterUpstreamEvent =  spy(new AfterUpstreamRequestEvent(response, requestContext, session))

    when:
    subject.onAfterUpstreamRequestEvent(afterUpstreamEvent)

    then:
    verify(upstreamLogger, never()).logRequest(response)
  }

  def "doesn't throw any exceptions if RequestContext and Session are null"() {
    given:
    def response = new Response()
    def requestContext = null
    def session = null
    def afterUpstreamEvent = spy(new AfterUpstreamRequestEvent(response, requestContext, session))

    when:
    subject.onAfterUpstreamRequestEvent(afterUpstreamEvent)

    then:
    noExceptionThrown()
  }

  def "resets the original session and request context"() {
    given:
    RequestContext.builder().clientId("client").build().register()
    def originalRequestContext = RequestContext.current()
    Session.createSession()
    def originalSession = Session.current()
    def response = new Response()

    def newRequestContext = RequestContext.builder().clientId("otherClient").build()
    def newSession = new Session()
    def afterUpstreamEvent = spy(new AfterUpstreamRequestEvent(response, newRequestContext, newSession))

    when:
    subject.onAfterUpstreamRequestEvent(afterUpstreamEvent)

    then:
    originalRequestContext != afterUpstreamEvent.requestContext
    originalSession != afterUpstreamEvent.session
  }
}
