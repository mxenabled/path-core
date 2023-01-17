package com.mx.path.model.context.facilities

import com.mx.common.collections.ObjectMap
import com.mx.path.model.context.facility.Facilities
import com.mx.testing.EncryptionServiceImpl
import com.mx.testing.EventBusImpl
import com.mx.testing.StoreImpl

import spock.lang.Specification

class FacilitiesTest extends Specification {

  def cleanup() {
    Facilities.reset()
  }

  def "don't allow eventBus overwrite"() {
    when:
    Facilities.addEventBus("client1", new EventBusImpl(new ObjectMap()))
    Facilities.addEventBus("client1", new EventBusImpl(new ObjectMap()))

    then:
    def ex = thrown(RuntimeException)
    ex.getMessage() == "Attempting to overwrite GatewayEventBus for client: client1. Only one can be registered. Use #getEventBus()."
  }
}
