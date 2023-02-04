package com.mx.path.model.context.events

import static org.mockito.Mockito.spy

import com.google.common.eventbus.Subscribe
import com.mx.common.events.GatewayEventBusException
import com.mx.path.model.context.event.GatewayEventBus

import spock.lang.Specification

class GatewayEventBusTest extends Specification {

  class TestEvent {
  }

  class TestSubscriber {
    public boolean triggered = false

    @Subscribe
    def doSomething(TestEvent event) {
      triggered = true
    }
  }

  def cleanup() {
  }

  def "invokes registered subscribers"() {
    given:
    def subject = new GatewayEventBus()
    def subscriber = spy(new TestSubscriber())
    subject.register(subscriber)
    def event = new TestEvent()

    when:
    subject.post(event)

    then:
    subscriber.triggered
  }

  class BadSubscriber {
    public void randomEvent(Object whatever) {}
  }

  def "register fails if subscriber event type is invalid"() {
    given:
    def subject = new GatewayEventBus()
    def subscriber = new BadSubscriber()

    when:
    subject.register(subscriber)

    then:
    def ex = thrown(GatewayEventBusException)
    ex.getMessage() == "Invalid event bus subscriber class - class com.mx.path.model.context.events.GatewayEventBusTest\$BadSubscriber has no methods annotated @Subscriber"
  }
}
