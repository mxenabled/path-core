package com.mx.path.gateway.events

import static org.mockito.Mockito.spy

import com.google.common.eventbus.Subscribe
import com.mx.path.model.context.RequestContext
import com.mx.path.model.context.facility.Facilities

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
  }

  def cleanup() {
    Facilities.reset()
  }

  def "invokes registered subscribers"() {
    given:
    def subject = new GatewayEventBus()
    def subscriber = spy(new TestSubscriber())
    subject.register(subscriber)
    def event = new BeforeAccessorEvent(null, null, null)

    when:
    subject.post(event)
    subject.post(new AfterAccessorEvent(null, null, null))

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
    ex.getMessage() == "Invalid event bus subscriber - com.mx.path.gateway.events.GatewayEventBusTest.BadSubscriber.randomEvent handles event type com.mx.path.model.context.RequestContext which does not implement GatewayEvent or AccessorEvent"
  }
}
