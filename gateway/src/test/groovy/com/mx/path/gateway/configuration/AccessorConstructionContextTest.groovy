package com.mx.path.gateway.configuration

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.common.connect.AccessorConnectionSettings
import com.mx.path.gateway.accessor.AccessorConfiguration
import com.mx.testing.AccountAccessorImpl
import com.mx.testing.binding.AccessorWithBoundConfiguration
import com.mx.testing.binding.AccessorWithBoundConnections

import spock.lang.Specification

class AccessorConstructionContextTest extends Specification {

  def "constructs basic accessor"() {
    given:
    def configuration = AccessorConfiguration.builder().clientId("client1").build()
    def subject = new AccessorConstructionContext(AccountAccessorImpl, configuration)

    when:
    def result = subject.build()

    then:
    result != null
    result.class == AccountAccessorImpl
    result.getConfiguration() == configuration
  }

  def "constructs accessor with bound configuration"() {
    given:
    def configuration = AccessorConfiguration.builder().clientId("client1").configuration("key1", "value1").build()
    AccessorConstructionContext<AccessorWithBoundConfiguration> subject = new AccessorConstructionContext<>(AccessorWithBoundConfiguration, configuration)

    when:
    def result = subject.build()

    then:
    result.getConfigurationObj() != null
    result.getConfigurationObj().key1 == "value1"
  }

  def "describes accessor"() {
    given:
    def configuration = AccessorConfiguration.builder()
        .clientId("client1")
        .configuration("key1", "value1")
        .connection("connection1", AccessorConnectionSettings.builder().baseUrl("http://connection1/api").configuration("key1", "connection1Value1").build())
        .connection("connection2", AccessorConnectionSettings.builder().baseUrl("http://connection2/api").configuration("key1", "connection2Value1").build())
        .build()
    AccessorConstructionContext<AccessorWithBoundConnections> subject = new AccessorConstructionContext<>(AccessorWithBoundConnections, configuration)

    when:
    def description = new ObjectMap()
    subject.describe(description)

    then:
    description.getMap("configurations").getArray(":bindings:") != null
    description.getMap("connections").getMap(":bindings:") != null
  }

  def "constructs accessor with connections"() {
    given:
    def configuration = AccessorConfiguration.builder()
        .clientId("client1")
        .configuration("key1", "value1")
        .connection("connection1", AccessorConnectionSettings.builder().baseUrl("http://connection1/api").configuration("key1", "connection1Value1").build())
        .connection("connection2", AccessorConnectionSettings.builder().baseUrl("http://connection2/api").configuration("key1", "connection2Value1").build())
        .build()

    AccessorConstructionContext<AccessorWithBoundConnections> subject = new AccessorConstructionContext<>(AccessorWithBoundConnections, configuration)

    when:
    def result = subject.build()

    then:
    result.getConfigs().getKey1() == "value1"
    result.getConnection1().getBaseUrl() == "http://connection1/api"
    result.getConnection1().getConfigs().getKey1() == "connection1Value1"
    result.getConnection2().getBaseUrl() == "http://connection2/api"
    result.getConnection2().getConfigs().getKey1() == "connection2Value1"
  }
}
