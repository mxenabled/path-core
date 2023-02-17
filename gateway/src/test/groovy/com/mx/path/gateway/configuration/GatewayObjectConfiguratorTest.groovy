package com.mx.path.gateway.configuration

import com.mx.common.collections.ObjectMap
import com.mx.common.messaging.MessageBroker
import com.mx.path.gateway.behavior.GatewayBehavior
import com.mx.path.gateway.service.GatewayService
import com.mx.testing.MessageBrokerImpl
import com.mx.testing.binding.BehaviorWithClientIDAndConfiguration
import com.mx.testing.binding.BindedConfigGatewayService

import spock.lang.Specification

class GatewayObjectConfiguratorTest extends Specification {
  GatewayObjectConfigurator subject
  ConfigurationState state

  def setup() {
    state = ConfigurationState.current
    state.pushLevel("client1")
    subject = new GatewayObjectConfigurator(state)
  }

  def cleanup() {
    ConfigurationState.resetCurrent()
  }

  def "builds and binds a GatewayService"() {
    given:
    def node = new ObjectMap().tap {
      put("class", BindedConfigGatewayService.getCanonicalName())
      put("configurations", new ObjectMap().tap { put("string", "value") })
    }

    when:
    def result = subject.buildFromNode(node, "client1", GatewayService)

    then:
    result instanceof BindedConfigGatewayService
    verifyAll((BindedConfigGatewayService) result) {
      config.string == "value"
      config.clientId == "client1"
      initialize
    }
  }

  def "builds and binds a GatewayBehavior"() {
    given:
    def node = new ObjectMap().tap {
      put("class", BehaviorWithClientIDAndConfiguration.getCanonicalName())
      put("configurations", new ObjectMap().tap {
        put("active", true)
        put("actionFilter", "put")
      })
    }

    when:
    def result = subject.buildFromNode(node, "client1", GatewayBehavior)

    then:
    result instanceof BehaviorWithClientIDAndConfiguration
    verifyAll((BehaviorWithClientIDAndConfiguration) result) {
      behaviorConfiguration.active
      clientId == "client1"
      initialized
    }
  }

  def "builds and binds a Facility"() {
    given:
    def node = new ObjectMap().tap {
      put("class", MessageBrokerImpl.getCanonicalName())
      put("configurations", new ObjectMap().tap { put("key1", "value1") })
    }

    when:
    def result = subject.buildFromNode(node, "client1", MessageBroker)

    then:
    result instanceof MessageBrokerImpl
    verifyAll((MessageBrokerImpl) result) {
      configurations.testField == "value1"
      configurations.clientId == "client1"
      initialized
    }
  }
}
