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

  def "populates"() {
    given:
    ObjectMap facilitiesConfig = new ObjectMap()

    def cacheStoreConfig = facilitiesConfig.createMap("cacheStore")
    cacheStoreConfig.put("class", "com.mx.testing.StoreImpl")
    cacheStoreConfig.createMap("configurations").put("key1", "value1")

    def sessionStoreConfig = facilitiesConfig.createMap("sessionStore")
    sessionStoreConfig.put("class", "com.mx.testing.StoreImpl")
    sessionStoreConfig.createMap("configurations").put("key2", "value2")

    def secretStoreConfig = facilitiesConfig.createMap("secretStore")
    secretStoreConfig.put("class", "com.mx.testing.StoreImpl")
    secretStoreConfig.createMap("configurations").put("key3", "value3")

    def encryptionServiceConfig = facilitiesConfig.createMap("encryptionService")
    encryptionServiceConfig.put("class", "com.mx.testing.EncryptionServiceImpl")
    encryptionServiceConfig.createMap("configurations").put("key4", "value4")

    def eventBusConfig = facilitiesConfig.createMap("eventBus")
    eventBusConfig.put("class", "com.mx.testing.EventBusImpl")
    eventBusConfig.createMap("configurations").put("key5", "value5")

    when:
    Facilities.populate("client1", facilitiesConfig)

    then:
    Facilities.getCacheStore("client1").class == StoreImpl
    ((StoreImpl) Facilities.getCacheStore("client1")).getConfigurations().get("key1") == "value1"
    Facilities.getSessionStore("client1").class == StoreImpl
    Facilities.getSecretStore("client1").class == StoreImpl
    Facilities.getEncryptionService("client1").class == EncryptionServiceImpl
    Facilities.getEventBus("client1").class == EventBusImpl
    ((EncryptionServiceImpl) Facilities.getEncryptionService("client1")).getConfigurations().get("key4") == "value4"
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
