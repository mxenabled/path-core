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

  def "global event bus"() {
    when:
    def clientEventBus= Facilities.getEventBus("whatever")
    def globalEventBus= Facilities.getEventBus()

    then: "same event bus"
    clientEventBus == globalEventBus

    when:
    Facilities.reset()

    then: "is unaffected by reset"
    globalEventBus == Facilities.getEventBus()

    when:
    Facilities.addEventBus("whatever", null)

    then: "is unaffected by set"
    globalEventBus == Facilities.getEventBus()
  }
}
