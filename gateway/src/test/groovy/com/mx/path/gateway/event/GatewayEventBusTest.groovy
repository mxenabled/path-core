package com.mx.path.gateway.event

import static org.mockito.Mockito.spy

import com.google.common.eventbus.Subscribe
import com.mx.path.core.context.RequestContext
import com.mx.path.core.context.facility.Facilities
import com.mx.testing.TestSubscriber

import spock.lang.Specification

class GatewayEventBusTest extends Specification {

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

  def "safe register only registers once"() {
    given:
    def subject = new GatewayEventBus()

    when:
    def result = subject.registerByClass(TestSubscriber.class)

    then:
    result

    when:
    result = subject.registerByClass(TestSubscriber.class)

    then:
    !result
  }
}
