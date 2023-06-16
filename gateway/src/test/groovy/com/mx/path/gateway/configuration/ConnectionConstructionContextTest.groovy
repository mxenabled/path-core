package com.mx.path.gateway.configuration

import com.mx.path.core.common.connect.AccessorConnectionSettings
import com.mx.testing.binding.BasicConnection
import com.mx.testing.binding.ConnectionWithBoundConfiguration
import com.mx.testing.binding.ConnectionWithInvalidConstructor

import spock.lang.Specification

class ConnectionConstructionContextTest extends Specification {
  ConfigurationState state

  def setup() {
    state = ConfigurationState.getCurrent()
  }

  def cleanup() {
    ConfigurationState.resetCurrent()
  }

  def "binds connection attributes"() {
    given:
    def connection = AccessorConnectionSettings.builder()
        .certificateAlias("alias")
        .configuration("key1", "value1")
        .keystorePath("/right/here")
        .keystorePassword("LetM3!n".toCharArray())
        .build()
    def subject = new ConnectionConstructionContext("client1", state, BasicConnection.class, connection)

    when:
    def result = subject.build()

    then:
    connection.getBaseUrl() == result.getBaseUrl()
    connection.getSkipHostNameVerify() == result.getSkipHostNameVerify()
    connection.getKeystorePassword() == result.getKeystorePassword()
    connection.getKeystorePath() == result.getKeystorePath()
    connection.getCertificateAlias() == result.getCertificateAlias()
  }

  def "binds connection with configuration"() {
    given:
    def connection = AccessorConnectionSettings.builder()
        .certificateAlias("alias")
        .configuration("key1", "value1")
        .keystorePath("/right/here")
        .keystorePassword("LetM3!n".toCharArray())
        .build()
    def subject = new ConnectionConstructionContext("client1", state, ConnectionWithBoundConfiguration.class, connection)

    when:
    def result = subject.build()

    then:
    result.getConfigs().getKey1() == "value1"
    connection.getBaseUrl() == result.getBaseUrl()
    connection.getSkipHostNameVerify() == result.getSkipHostNameVerify()
    connection.getKeystorePassword() == result.getKeystorePassword()
    connection.getKeystorePath() == result.getKeystorePath()
    connection.getCertificateAlias() == result.getCertificateAlias()
  }

  def "fails on connection with invalid constructor"() {
    given:
    state.pushLevel("connection1")
    def accessorConnectionSettings = AccessorConnectionSettings.builder()
        .build()

    when:
    new ConnectionConstructionContext("client1", state, ConnectionWithInvalidConstructor .class, accessorConnectionSettings)

    then:
    def error = thrown(ConfigurationError)
    error != null
    error.getMessage() == "No valid constructors for com.mx.testing.binding.ConnectionWithInvalidConstructor at connection1"
  }
}
