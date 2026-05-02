package com.mx.path.gateway.configuration

import java.time.Duration

import com.mx.path.core.common.accessor.PathResponseStatus
import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.common.configuration.ConfigurationException
import com.mx.path.core.common.gateway.GatewayException
import com.mx.testing.binding.ConnectionWithBoundConfiguration

import spock.lang.Specification
import spock.lang.Unroll

class ConnectionBinderTest extends Specification {

  ObjectMap configuration
  ConnectionBinder subject

  def setup() {
    subject = new ConnectionBinder()
    configuration = new ObjectMap()
  }

  def "build connection"() {
    given:
    def configuration = new ObjectMap().tap {
      createMap("TestConnection").tap {
        createMap("configurations").tap {
          put("clientId", "clientId")
        }
        put("baseUrl", "url")
        put("certificateAlias", "alias")
        put("keystorePath", "path")
        put("keystorePassword", "password")
      }
    }

    when:
    def connection = (ConnectionWithBoundConfiguration) subject.build(ConnectionWithBoundConfiguration.class, configuration, "TestConnection")

    then:
    verifyAll (connection) {
      baseUrl == "url"
      certificateAlias == "alias"
      keystorePath == "path"
      keystorePassword.toString() == "password"
      connectTimeout == null
      requestTimeout == null
      !skipHostNameVerify
    }
  }

  def "build connection throw on missing client id"() {
    given:
    def configuration = new ObjectMap().tap {
      createMap("TestConnection").tap {
        createMap("configurations").tap {
          put("clientId", null)
        }
        put("baseUrl", "url")
        put("certificateAlias", "alias")
        put("keystorePath", "path")
        put("keystorePassword", "password")
      }
    }

    when:
    subject.build(ConnectionWithBoundConfiguration.class, configuration, "TestConnection")

    then:
    def e = thrown(ConfigurationError)
    e.message == "Client ID not provided for connection TestConnection at connections.TestConnection"
  }

  def "build connection settings"() {
    given:
    def configuration = new ObjectMap().tap {
      createMap("TestConnection").tap {
        put("baseUrl", "url")
        put("certificateAlias", "alias")
        put("keystorePath", "path")
        put("keystorePassword", "password")
        put("connectTimeout", "500ms")
        put("requestTimeout", "20s")
        put("skipHostNameVerify", true)
      }
    }

    when:
    def connection = subject.buildConnection(configuration, "TestConnection")

    then:
    verifyAll (connection) {
      baseUrl == "url"
      certificateAlias == "alias"
      keystorePath == "path"
      keystorePassword.toString() == "password"
      connectTimeout == Duration.ofMillis(500)
      requestTimeout == Duration.ofSeconds(20)
      skipHostNameVerify
    }
  }

  @Unroll
  def "build connection throws on invalid #fieldName format"(String fieldName) {
    given:
    def configuration = new ObjectMap().tap {
      createMap("TestConnection").tap {
        put("baseUrl", "url")
        put(fieldName, "not-a-valid-duration")
      }
    }

    when:
    subject.buildConnection(configuration, "TestConnection")

    then:
    def e = thrown(ConfigurationException)
    e.message == "Invalid duration string: not-a-valid-duration"

    where:
    fieldName << [
      "connectTimeout",
      "requestTimeout"
    ]
  }

  def "build connection and fail validation"() {
    given:
    def configuration = new ObjectMap().tap {
      createMap("TestConnection").tap {
        put("baseUrl", "url")
        put("certificateAlias", "")
        put("keystorePath", "path")
        put("keystorePassword", "password")
      }
    }

    when:
    subject.buildConnection(configuration, "TestConnection")

    then:
    def e = thrown(GatewayException)
    e.status == PathResponseStatus.INTERNAL_ERROR
    e.message == "Invalid connection details. Missing certificateAlias"
  }
}
