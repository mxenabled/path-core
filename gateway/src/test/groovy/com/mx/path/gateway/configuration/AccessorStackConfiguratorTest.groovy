package com.mx.path.gateway.configuration

import com.mx.accessors.AccessorConfiguration
import com.mx.accessors.BaseAccessor
import com.mx.common.collections.ObjectMap
import com.mx.path.gateway.accessor.proxy.BaseAccessorProxyPrototype
import com.mx.path.gateway.accessor.proxy.account.AccountBaseAccessorProxyPrototype
import com.mx.path.gateway.accessor.proxy.account.AccountBaseAccessorProxySingleton
import com.mx.path.gateway.api.account.AccountGateway
import com.mx.testing.BaseAccessorImpl

import spock.lang.Specification

class AccessorStackConfiguratorTest extends Specification {
  AccountGateway.AccountGatewayBuilder builder
  BaseAccessorProxyPrototype parent
  ConfigurationState state
  AccessorStackConfigurator subject

  def setup() {
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
      put("class", com.mx.testing.binding.ScopePrototypeAccessor.class.getCanonicalName())
    }
    def accessor = subject.buildAccessor("accounts", node, "client1", builder, parent)

    then:
    accessor.class == AccountBaseAccessorProxyPrototype.class

    when:
    node = new ObjectMap()
    node.createMap("accessor").tap {
      put("class", com.mx.testing.binding.ScopeSingletonAccessor.class.getCanonicalName())
    }
    accessor = subject.buildAccessor("accounts", node, "client1", builder, parent)

    then:
    accessor.class == AccountBaseAccessorProxySingleton.class
  }

  def "determines scope from configuration"() {
    when:
    def node = new ObjectMap()
    node.createMap("accessor").tap {
      put("class", com.mx.testing.binding.ScopeSingletonAccessor.class.getCanonicalName())
      put("scope", "prototype")
    }
    def accessor = subject.buildAccessor("accounts", node, "client1", builder, parent)

    then:
    accessor.class == AccountBaseAccessorProxyPrototype.class

    when:
    node = new ObjectMap()
    node.createMap("accessor").tap {
      put("class", com.mx.testing.binding.ScopePrototypeAccessor.class.getCanonicalName())
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
      put("class", com.mx.testing.binding.ScopeSingletonAccessor.class.getCanonicalName())
      put("scope", "garbage")
    }
    subject.buildAccessor("accounts", node, "client1", builder, parent)

    then:
    def error = thrown(ConfigurationError)
    error.getMessage() == "Invalid scope (garbage) on scope at accessors.accounts"
  }
}
