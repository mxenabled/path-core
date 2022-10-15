package com.mx.gateway

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

import com.mx.path.model.context.RequestContext
import com.mx.testing.StaticContextCaptureBehavior
import com.mx.testing.gateway.api.Gateway
import com.mx.testing.gateway.api.GatewayConfigurator

import spock.lang.Specification

/**
 * Tests generated code that sets up the gateway request context to ensure values are set correctly.
 */
class GatewayRequestContextTest extends Specification {
  Map<String, Gateway> gateways
  Gateway subject
  String gatewayClientId = "client1"

  def setup() {
    gateways = new GatewayConfigurator().buildFromYaml(new String(Files.readAllBytes(Path.of("./src/test/resources/GatewayRequestContextTest.yaml")), StandardCharsets.UTF_8))
    subject = gateways.get(gatewayClientId)
  }

  def cleanup() {
    RequestContext.clear()
    StaticContextCaptureBehavior.reset()
  }

  /**
   * Calls gateway and returns captured GatewayRequestContext
   * @return captured GatewayRequestContext
   */
  def captureContext() {
    subject.accounts().get("A-1234")
    StaticContextCaptureBehavior.capturedContext
  }

  def "sets clientId"() {
    when: "no request context registered"
    RequestContext.clear()

    then: "uses gateway.yaml clientId"
    captureContext().getClientId() == gatewayClientId

    when: "request context registered with no clientId"
    RequestContext.builder().build().register()

    then: "uses gateway.yaml clientId"
    captureContext().getClientId() == gatewayClientId

    when: "request context registered with different clientId"
    RequestContext.builder().clientId("client2").build().register()

    then: "uses request context clientId"
    captureContext().getClientId() == "client2"
  }
}
