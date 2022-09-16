package com.mx.gateway.configuration

import com.mx.common.accessors.AccessorConfiguration
import com.mx.common.collections.ObjectMap
import com.mx.path.gateway.accessor.proxy.AccountBaseAccessorProxyPrototype
import com.mx.path.gateway.accessor.proxy.AccountBaseAccessorProxySingleton
import com.mx.path.gateway.accessor.proxy.BaseAccessorProxyPrototype
import com.mx.path.gateway.configuration.AccessorStackConfigurator
import com.mx.path.gateway.configuration.ConfigurationError
import com.mx.path.gateway.configuration.ConfigurationState
import com.mx.testing.BaseAccessorImpl
import com.mx.testing.binding.ScopePrototypeAccessor
import com.mx.testing.binding.ScopeSingletonAccessor
import com.mx.testing.gateway.api.AccountGateway
import com.mx.testing.gateway.api.Gateway

import spock.lang.Specification

class AccessorStackConfiguratorTest extends Specification {
  AccountGateway.AccountGatewayBuilder builder
  BaseAccessorProxyPrototype parent
  ConfigurationState state
  AccessorStackConfigurator subject
  Gateway gateway

  def setup() {
    gateway = new Gateway() // This is here to make sure we load the accessor proxy mappings
    parent = new BaseAccessorProxyPrototype(AccessorConfiguration.builder().build(), BaseAccessorImpl.class)
    builder = AccountGateway.builder()
    state = ConfigurationState.current
    state.pushLevel("accessors")
    subject = new AccessorStackConfigurator(state)
    subject.setRootAccessor(parent)
  }

  def cleanup() {
    ConfigurationState.resetCurrent()
  }

  def "determines scope from class"() {
    when:
    def node = new ObjectMap()
    node.createMap("accessor").tap {
      put("class", ScopePrototypeAccessor.class.getCanonicalName())
    }
    def accessor = subject.buildAccessor("accounts", node, "client1", builder, parent)

    then:
    accessor.class == AccountBaseAccessorProxyPrototype.class

    when:
    node = new ObjectMap()
    node.createMap("accessor").tap {
      put("class", ScopeSingletonAccessor.class.getCanonicalName())
    }
    accessor = subject.buildAccessor("accounts", node, "client1", builder, parent)

    then:
    accessor.class == AccountBaseAccessorProxySingleton.class
  }

  def "determines scope from configuration"() {
    when:
    def node = new ObjectMap()
    node.createMap("accessor").tap {
      put("class", ScopeSingletonAccessor.class.getCanonicalName())
      put("scope", "prototype")
    }
    def accessor = subject.buildAccessor("accounts", node, "client1", builder, parent)

    then:
    accessor.class == AccountBaseAccessorProxyPrototype.class

    when:
    node = new ObjectMap()
    node.createMap("accessor").tap {
      put("class", ScopePrototypeAccessor.class.getCanonicalName())
      put("scope", "singleton")
    }
    subject.buildAccessor("accounts", node, "client1", builder, parent)

    then:
    def error = thrown(ConfigurationError)
    error.getMessage() == "Configured scope (singleton) is higher that specified MaxScope (prototype) on scope at accessors.accounts"
  }

  def "errors on invalid scope"() {
    when:
    def node = new ObjectMap()
    node.createMap("accessor").tap {
      put("class", ScopeSingletonAccessor.class.getCanonicalName())
      put("scope", "garbage")
    }
    subject.buildAccessor("accounts", node, "client1", builder, parent)

    then:
    def error = thrown(ConfigurationError)
    error.getMessage() == "Invalid scope (garbage) on scope at accessors.accounts"
  }
}
