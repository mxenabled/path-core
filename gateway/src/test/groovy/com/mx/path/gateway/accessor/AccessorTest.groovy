package com.mx.path.gateway.accessor

import com.mx.path.core.common.accessor.BadRequestException
import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.gateway.accessor.Accessor
import com.mx.path.gateway.accessor.AccessorConfiguration
import com.mx.path.gateway.accessor.AccessorConnections
import com.mx.testing.BaseAccessorImpl

import spock.lang.Specification

class AccessorTest extends Specification {

  static class ConcreteAccessor extends Accessor {
  }

  // Subclass that calls the deprecated super(config) constructor
  static class ConfiguredAccessor extends Accessor {
    ConfiguredAccessor(AccessorConfiguration config) {
      super(config)
    }
  }

  def "getChildAccessors returns empty list when no @API child methods"() {
    given:
    def accessor = new ConcreteAccessor()

    expect:
    accessor.getChildAccessors().isEmpty()
  }

  def "getChildAccessors succeeds and returns entries when @API methods return accessors"() {
    given:
    def accessor = new BaseAccessorImpl()

    when:
    def result = accessor.getChildAccessors()

    then:
    !result.isEmpty()
  }

  def "getChildAccessors ignores methods that throw NOT_IMPLEMENTED AccessorException"() {
    given:
    // BaseAccessor.accounts() throws AccessorMethodNotImplementedException (NOT_IMPLEMENTED status)
    // when no accounts accessor is set — our overriding subclass does this path
    def accessor = new BaseAccessorImpl() {
          @Override
          com.mx.testing.accessors.AccountBaseAccessor accounts() {
            throw new com.mx.path.core.common.accessor.AccessorMethodNotImplementedException()
          }
        }

    when:
    def result = accessor.getChildAccessors()

    then:
    noExceptionThrown()
  }

  def "getChildAccessors logs when method throws non-NOT_IMPLEMENTED AccessorException"() {
    given:
    // BadRequestException has status BAD_REQUEST, not NOT_IMPLEMENTED
    def accessor = new BaseAccessorImpl() {
          @Override
          com.mx.testing.accessors.AccountBaseAccessor accounts() {
            throw new BadRequestException("unexpected error")
          }
        }

    when:
    def result = accessor.getChildAccessors()

    then:
    noExceptionThrown()
  }

  def "getAccessorBase returns the direct Accessor subclass"() {
    expect:
    Accessor.getAccessorBase(ConcreteAccessor) == ConcreteAccessor
  }

  def "getBaseChildAccessorMethods returns empty for simple accessor"() {
    expect:
    Accessor.getBaseChildAccessorMethods(ConcreteAccessor).isEmpty()
  }

  def "getBaseChildAccessorMethods returns @API methods from base"() {
    when:
    def methods = Accessor.getBaseChildAccessorMethods(BaseAccessorImpl)

    then:
    !methods.isEmpty()
  }

  def "deprecated constructor sets configuration"() {
    given:
    def config = AccessorConfiguration.builder().clientId("c1").configurations(new ObjectMap()).build()

    when:
    def accessor = new ConfiguredAccessor(config)

    then:
    accessor.getConfiguration().is(config)
  }

  def "AccessorConnections add and get connection"() {
    given:
    def connections = new AccessorConnections()
    def settings = new com.mx.path.core.common.connect.AccessorConnectionSettings()

    when:
    connections.addConnection("primary", settings)

    then:
    connections.getConnection("primary").is(settings)
    connections.getConnection("missing") == null
  }

  def "AccessorConfiguration builder with connection and configuration helpers"() {
    given:
    def settings = new com.mx.path.core.common.connect.AccessorConnectionSettings()

    when:
    def config = AccessorConfiguration.builder()
        .clientId("client1")
        .connection("primary", settings)
        .configuration("timeout", "30")
        .build()

    then:
    config.getClientId() == "client1"
    config.getConnections().getConnection("primary").is(settings)
    config.getConfigurations().get("timeout") == "30"
  }

  def "AccessorConfiguration.describe() returns populated ObjectMap"() {
    given:
    def config = AccessorConfiguration.builder()
        .clientId("client1")
        .configuration("host", "localhost")
        .build()

    when:
    def desc = config.describe()

    then:
    desc.get("clientId") == "client1"
    desc.containsKey("configurations")
  }

  def "AccessorConfiguration.describe(ObjectMap) fills provided map"() {
    given:
    def config = AccessorConfiguration.builder()
        .clientId("client2")
        .build()
    def description = new ObjectMap()

    when:
    config.describe(description)

    then:
    description.get("clientId") == "client2"
  }

  def "setConfiguration and getConfiguration round-trip"() {
    given:
    def accessor = new ConcreteAccessor()
    def config = AccessorConfiguration.builder().clientId("c1").configurations(new ObjectMap()).build()

    when:
    accessor.setConfiguration(config)

    then:
    accessor.getConfiguration().is(config)
  }
}
