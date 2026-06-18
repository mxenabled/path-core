package com.mx.path.gateway

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.gateway.accessor.AccessorResponse
import com.mx.path.gateway.behavior.GatewayBehavior
import com.mx.path.gateway.context.GatewayRequestContext
import com.mx.path.gateway.service.GatewayService
import com.mx.testing.MinimalAccessor
import com.mx.testing.gateway.MinimalGateway

import spock.lang.Specification

class GatewayBuilderHelperTest extends Specification {

  def "getBuilder returns a builder for the given class"() {
    when:
    def builder = GatewayBuilderHelper.getBuilder(MinimalGateway)

    then:
    builder != null
  }

  def "setRootAccessor sets the base accessor on the builder"() {
    given:
    def builder = GatewayBuilderHelper.getBuilder(MinimalGateway)
    def accessor = new MinimalAccessor()

    when:
    GatewayBuilderHelper.setRootAccessor(builder, accessor)
    def gateway = GatewayBuilderHelper.build(builder, MinimalGateway)

    then:
    gateway.getBaseAccessor().is(accessor)
  }

  def "setClientId sets the client id on the builder"() {
    given:
    def builder = GatewayBuilderHelper.getBuilder(MinimalGateway)
    GatewayBuilderHelper.setRootAccessor(builder, new MinimalAccessor())

    when:
    GatewayBuilderHelper.setClientId(builder, "client1")
    def gateway = GatewayBuilderHelper.build(builder, MinimalGateway)

    then:
    gateway.getClientId() == "client1"
  }

  def "addBehavior adds a behavior to the builder"() {
    given:
    def builder = GatewayBuilderHelper.getBuilder(MinimalGateway)
    GatewayBuilderHelper.setRootAccessor(builder, new MinimalAccessor())
    def behavior = new GatewayBehavior(new ObjectMap()) {
          protected <T> AccessorResponse<T> call(Class<T> r, GatewayRequestContext req, GatewayBehavior t) {
            new AccessorResponse<T>()
          }
        }

    when:
    GatewayBuilderHelper.addBehavior(builder, behavior)
    def gateway = GatewayBuilderHelper.build(builder, MinimalGateway)

    then:
    gateway.getBehaviors().size() == 1
  }

  def "addService adds a service to the builder"() {
    given:
    def builder = GatewayBuilderHelper.getBuilder(MinimalGateway)
    GatewayBuilderHelper.setRootAccessor(builder, new MinimalAccessor())
    def service = new GatewayService(new ObjectMap()) {
          void start() {}
          void stop() {}
        }

    when:
    GatewayBuilderHelper.addService(builder, service)
    def gateway = GatewayBuilderHelper.build(builder, MinimalGateway)

    then:
    gateway.getServices().size() == 1
  }
}
