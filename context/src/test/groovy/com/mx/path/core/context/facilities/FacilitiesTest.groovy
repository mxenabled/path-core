package com.mx.path.core.context.facilities

import static org.mockito.Mockito.mock

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.common.exception.ExceptionReporter
import com.mx.path.core.common.messaging.MessageBroker
import com.mx.path.core.common.process.FaultTolerantExecutor
import com.mx.path.core.common.security.EncryptionService
import com.mx.path.core.context.facility.Facilities
import com.mx.testing.EventBusImpl
import com.mx.testing.StoreImpl

import spock.lang.Specification

class FacilitiesTest extends Specification {

  def cleanup() {
    Facilities.reset()
  }

  def "setCacheStore and getCacheStore"() {
    given:
    def store = new StoreImpl(new ObjectMap())

    when:
    Facilities.setCacheStore("client1", store)

    then:
    Facilities.getCacheStore("client1").is(store)
    Facilities.getCacheStore("unknown") == null
  }

  def "setEncryptionService and getEncryptionService"() {
    given:
    def svc = mock(EncryptionService)

    when:
    Facilities.setEncryptionService("client1", svc)

    then:
    Facilities.getEncryptionService("client1").is(svc)
    Facilities.getEncryptionService("unknown") == null
  }

  def "setExceptionReporter and getExceptionReporter"() {
    given:
    def reporter = mock(ExceptionReporter)

    when:
    Facilities.setExceptionReporter("client1", reporter)

    then:
    Facilities.getExceptionReporter("client1").is(reporter)
    Facilities.getExceptionReporter("unknown") == null
  }

  def "setFaultTolerantExecutor and getFaultTolerantExecutor"() {
    given:
    def executor = mock(FaultTolerantExecutor)

    when:
    Facilities.setFaultTolerantExecutor("client1", executor)

    then:
    Facilities.getFaultTolerantExecutor("client1").is(executor)
    Facilities.getFaultTolerantExecutor("unknown") == null
  }

  def "setMessageBroker and getMessageBroker"() {
    given:
    def broker = mock(MessageBroker)

    when:
    Facilities.setMessageBroker("client1", broker)

    then:
    Facilities.getMessageBroker("client1").is(broker)
    Facilities.getMessageBroker("unknown") == null
  }

  def "setSecretStore and getSecretStore"() {
    given:
    def store = new StoreImpl(new ObjectMap())

    when:
    Facilities.setSecretStore("client1", store)

    then:
    Facilities.getSecretStore("client1").is(store)
    Facilities.getSecretStore("unknown") == null
  }

  def "setSessionStore and getSessionStore"() {
    given:
    def store = new StoreImpl(new ObjectMap())

    when:
    Facilities.setSessionStore("client1", store)

    then:
    Facilities.getSessionStore("client1").is(store)
    Facilities.getSessionStore("unknown") == null
  }

  def "addEventBus and getEventBus"() {
    given:
    def bus = new EventBusImpl(new ObjectMap())

    when:
    Facilities.addEventBus("client1", bus)

    then:
    Facilities.getEventBus("client1").is(bus)
    Facilities.getEventBus("unknown") == null
  }

  def "addEventBus throws on duplicate client"() {
    when:
    Facilities.addEventBus("client1", new EventBusImpl(new ObjectMap()))
    Facilities.addEventBus("client1", new EventBusImpl(new ObjectMap()))

    then:
    def ex = thrown(RuntimeException)
    ex.getMessage() == "Attempting to overwrite GatewayEventBus for client: client1. Only one can be registered. Use #getEventBus()."
  }

  def "reset clears all facilities"() {
    given:
    def store = new StoreImpl(new ObjectMap())
    Facilities.setCacheStore("client1", store)
    Facilities.setSessionStore("client1", store)
    Facilities.setSecretStore("client1", store)
    Facilities.addEventBus("client1", new EventBusImpl(new ObjectMap()))
    Facilities.setEncryptionService("client1", mock(EncryptionService))
    Facilities.setMessageBroker("client1", mock(MessageBroker))

    when:
    Facilities.reset()

    then:
    Facilities.getCacheStore("client1") == null
    Facilities.getSessionStore("client1") == null
    Facilities.getSecretStore("client1") == null
    Facilities.getEventBus("client1") == null
    Facilities.getEncryptionService("client1") == null
    Facilities.getMessageBroker("client1") == null
  }

  def "describe includes configured facilities with getConfigurations"() {
    given:
    def store = new StoreImpl(new ObjectMap())
    Facilities.setCacheStore("client1", store)
    Facilities.setSessionStore("client1", store)

    def description = new ObjectMap()

    when:
    Facilities.describe("client1", description)

    then:
    description.getMap("cacheStore").get("class") == store.getClass().getCanonicalName()
    description.getMap("sessionStore").get("class") == store.getClass().getCanonicalName()
  }

  def "describe handles facility without getConfigurations method"() {
    given:
    // EncryptionService mock does not have getConfigurations, so describeFacility hits the catch branch
    def svc = mock(EncryptionService)
    Facilities.setEncryptionService("client1", svc)

    def description = new ObjectMap()

    when:
    Facilities.describe("client1", description)

    then:
    description.getMap("encryptionService").get("configurations") == "no description provided"
  }

  def "describe skips null facilities"() {
    given:
    def description = new ObjectMap()

    when:
    Facilities.describe("unknown", description)

    then:
    noExceptionThrown()
  }
}
