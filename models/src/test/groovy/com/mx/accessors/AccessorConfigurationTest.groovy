package com.mx.accessors

import com.mx.common.collections.ObjectMap

import spock.lang.Specification

class AccessorConfigurationTest extends Specification {

  def "builder"() {
    when:
    def subject = AccessorConfiguration.builder()
        .clientId("mx")
        .connection("connection", AccessorConnection.builder().build())
        .configuration("name", "value")
        .build()

    then:
    subject.getClientId() == "mx"
    subject.connections.getConnection("connection") != null
    subject.configurations.get("name") == "value"
  }

  def "describe"() {
    given:
    def subject = AccessorConfiguration.builder()
        .clientId("mx")
        .connection("connection_1", AccessorConnection.builder().baseUrl("http://localhost:9090").build())
        .configuration("name", "value")
        .build()
    when:
    def description =  new ObjectMap()
    subject.describe(description)

    then:
    description.get("clientId") == "mx"
    description.getMap("connections").getMap("connection_1").get("baseUrl") == "http://localhost:9090"
    description.getMap("configurations").get("name") == "value"
  }
}
