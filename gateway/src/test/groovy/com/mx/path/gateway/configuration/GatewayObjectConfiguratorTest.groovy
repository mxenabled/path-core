package com.mx.path.gateway.configuration

import com.mx.common.collections.ObjectMap
import com.mx.common.messaging.MessageBroker
import com.mx.path.gateway.behavior.GatewayBehavior
import com.mx.path.gateway.service.GatewayService
import com.mx.testing.MessageBrokerImpl
import com.mx.testing.binding.BehaviorWithClientIDAndConfiguration
import com.mx.testing.binding.BindedConfigGatewayService
import com.mx.testing.gateway.BaseGateway

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
    ((BindedConfigGatewayService) result).config.string == "value"
    ((BindedConfigGatewayService) result).config.clientId == "client1"
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
    ((BehaviorWithClientIDAndConfiguration) result).behaviorConfiguration.active
    ((BehaviorWithClientIDAndConfiguration) result).clientId == "client1"
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
    ((MessageBrokerImpl) result).configurations.testField == "value1"
    ((MessageBrokerImpl) result).configurations.clientId == "client1"
  }
}
