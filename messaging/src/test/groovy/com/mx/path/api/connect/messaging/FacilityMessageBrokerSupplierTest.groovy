package com.mx.path.api.connect.messaging

import com.mx.common.collections.ObjectMap
import com.mx.common.messaging.MessageBroker
import com.mx.common.messaging.MessageError
import com.mx.path.model.context.facility.Facilities
import com.mx.testing.fakes.FakeMessageBroker

import spock.lang.Specification

class FacilityMessageBrokerSupplierTest extends Specification {
  FacilityMessageBrokerSupplier subject

  def setup() {
    subject = new FacilityMessageBrokerSupplier("clientId")
  }

  def "retrieves the MessageBroker from Facilities"() {
    given:
    createMessageBrokerForClient("clientId")

    when:
    def messageBroker = subject.get()

    then:
    messageBroker instanceof MessageBroker
  }

  def "throws an exception if the clientId is null"() {
    given:
    subject = new FacilityMessageBrokerSupplier(null)

    createMessageBrokerForClient("clientId")

    when:
    subject.get()

    then:
    thrown(MessageError)
  }

  private void createMessageBrokerForClient(String clientId) {
    Facilities.setMessageBroker(clientId, new FakeMessageBroker(new ObjectMap()))
  }
}
