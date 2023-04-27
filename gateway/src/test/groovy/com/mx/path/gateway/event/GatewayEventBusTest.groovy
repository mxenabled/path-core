package com.mx.path.gateway.event

import static org.mockito.Mockito.spy

import com.google.common.eventbus.Subscribe
import com.mx.path.core.context.RequestContext
import com.mx.path.core.context.Session
import com.mx.path.core.context.facility.Facilities

import spock.lang.Specification

class GatewayEventBusTest extends Specification {

  class TestSubscriber implements GatewayEvent {
    public boolean triggered = false

    @Subscribe
    def doSomething(BeforeAccessorEvent event) {
      triggered = true
    }

    @Override
    RequestContext getRequestContext() {
      return null
    }

    @Override
    Session getSession() {
      return null
    }
  }

  def cleanup() {
    Facilities.reset()
  }

  def "invokes registered subscribers"() {
    given:
    def subject = new GatewayEventBus()
    def subscriber = spy(new TestSubscriber())
    subject.register(subscriber)
    def event = new BeforeAccessorEvent(null, null, null, null)

    when:
    subject.post(event)
    subject.post(new AfterAccessorEvent(null, null, null, null))

    then:
    subscriber.triggered
  }

  class BadSubscriber {
    @Subscribe
    public void randomEvent(RequestContext whatever) {}
  }

  def "register fails if subscriber event type is invalid"() {
    given:
    def subject = new GatewayEventBus()
    def subscriber = new BadSubscriber()

    when:
    subject.register(subscriber)

    then:
    def ex = thrown(GatewayEventBusException)
    ex.getMessage() == "Invalid event bus subscriber - com.mx.path.gateway.event.GatewayEventBusTest.BadSubscriber.randomEvent handles event type com.mx.path.core.context.RequestContext which does not implement GatewayEvent, AccessorEvent, or UpstreamRequestEvent"
  }
}
