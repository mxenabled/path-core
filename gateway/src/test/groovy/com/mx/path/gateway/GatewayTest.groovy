package com.mx.path.gateway

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.common.event.EventBus
import com.mx.path.core.context.RequestContext
import com.mx.path.core.context.facility.Facilities
import com.mx.path.gateway.accessor.AccessorResponse
import com.mx.path.gateway.behavior.GatewayBehavior
import com.mx.path.gateway.behavior.StartBehavior
import com.mx.path.gateway.context.GatewayRequestContext
import com.mx.path.gateway.service.GatewayService
import com.mx.testing.MinimalAccessor
import com.mx.testing.gateway.MinimalGateway
import com.mx.testing.gateway.MinimalRootGateway

import spock.lang.Specification

class GatewayTest extends Specification {

  def "isTopLevel returns true for @RootGateway annotated gateway"() {
    given:
    def gateway = MinimalRootGateway.builder().baseAccessor(new MinimalAccessor()).build()

    expect:
    gateway.isTopLevel()
  }

  def "isTopLevel returns false for plain gateway"() {
    given:
    def gateway = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()

    expect:
    !gateway.isTopLevel()
  }

  def "constructor sets clientId"() {
    given:
    def gateway = new MinimalGateway("client42")

    expect:
    gateway.getClientId() == "client42"
  }

  def "gateways returns empty list when no child gateway methods exist"() {
    given:
    def gateway = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()

    expect:
    gateway.gateways().isEmpty()
  }

  def "describe(ObjectMap) fills provided map without error"() {
    given:
    def gateway = MinimalGateway.builder()
        .baseAccessor(new MinimalAccessor())
        .clientId("testClient")
        .build()
    def description = new ObjectMap()

    when:
    gateway.describe(description)

    then:
    noExceptionThrown()
  }

  def "describe() returns an ObjectMap"() {
    given:
    def gateway = MinimalGateway.builder()
        .baseAccessor(new MinimalAccessor())
        .clientId("testClient")
        .build()

    when:
    def result = gateway.describe()

    then:
    result != null
  }

  def "startServices starts each service and sets gateway back-reference"() {
    given:
    def started = []
    def service = new GatewayService(new ObjectMap()) {
          void start() {
            started << "started"
          }
          void stop() {}
        }
    def gateway = MinimalGateway.builder()
        .baseAccessor(new MinimalAccessor())
        .service(service)
        .build()

    when:
    gateway.startServices()

    then:
    started == ["started"]
    service.getGateway().is(gateway)
  }

  def "registerRemotes runs without error when remote is null"() {
    given:
    def gateway = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()

    when:
    gateway.registerRemotes()

    then:
    noExceptionThrown()
  }

  def "builder allows adding behaviors"() {
    given:
    def behavior = new GatewayBehavior(new ObjectMap()) {
          protected <T> AccessorResponse<T> call(Class<T> r, GatewayRequestContext req, GatewayBehavior t) {
            new AccessorResponse<T>()
          }
        }
    def gateway = MinimalGateway.builder()
        .baseAccessor(new MinimalAccessor())
        .behavior(behavior)
        .build()

    expect:
    gateway.getBehaviors().size() == 1
  }

  def "setParent and getParent"() {
    given:
    def parent = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()
    def child = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()

    when:
    child.setParent(parent)

    then:
    noExceptionThrown()
  }

  def "root() returns self when @RootGateway and no parent"() {
    given:
    def gateway = MinimalRootGateway.builder().baseAccessor(new MinimalAccessor()).build()

    expect:
    gateway.root().is(gateway)
  }

  def "root() returns null when not @RootGateway and no parent"() {
    given:
    def gateway = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()

    expect:
    gateway.root() == null
  }

  def "root() delegates to parent"() {
    given:
    def root = MinimalRootGateway.builder().baseAccessor(new MinimalAccessor()).build()
    def child = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()
    child.setParent(root)

    expect:
    child.root().is(root)
  }

  def "afterAccessor does nothing when clientId is null"() {
    given:
    def gateway = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()
    def context = RequestContext.builder().build()

    when:
    gateway.afterAccessor(gateway, new MinimalAccessor(), context)

    then:
    noExceptionThrown()
  }

  def "beforeAccessor does nothing when no EventBus configured"() {
    given:
    def gateway = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()
    def context = RequestContext.builder().clientId("client1").build()

    when:
    gateway.beforeAccessor(gateway, new MinimalAccessor(), context)

    then:
    noExceptionThrown()

    cleanup:
    Facilities.reset()
  }

  def "buildStack via reflection returns a StartBehavior"() {
    given:
    def gateway = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()
    def buildStackMethod = Gateway.getDeclaredMethod("buildStack")
    buildStackMethod.setAccessible(true)

    when:
    def stack = buildStackMethod.invoke(gateway)

    then:
    stack instanceof StartBehavior
  }

  def "describe() for root gateway with clientId covers the isTopLevel branch"() {
    given:
    def gateway = MinimalRootGateway.builder()
        .baseAccessor(new MinimalAccessor())
        .clientId("client1")
        .build()

    when:
    def desc = gateway.describe()

    then:
    noExceptionThrown()
    desc != null

    cleanup:
    Facilities.reset()
  }

  def "describe() throws when baseAccessor is null on non-root gateway"() {
    given:
    def gateway = new MinimalGateway()  // no-arg constructor, baseAccessor is null

    when:
    gateway.describe()

    then:
    thrown(RuntimeException)
  }

  def "afterAccessor fires event when EventBus is present"() {
    given:
    def postedEvents = []
    def eventBus = new EventBus() {
          ObjectMap configurations = new ObjectMap()
          void post(Object event) {
            postedEvents << event
          }
          void register(Object subscriber) {}
        }
    def context = RequestContext.builder().clientId("client1").build()
    Facilities.addEventBus("client1", eventBus)
    def gateway = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()

    when:
    gateway.afterAccessor(gateway, new MinimalAccessor(), context)

    then:
    postedEvents.size() == 1

    cleanup:
    Facilities.reset()
  }

  def "beforeAccessor fires event when EventBus is present"() {
    given:
    def postedEvents = []
    def eventBus = new EventBus() {
          ObjectMap configurations = new ObjectMap()
          void post(Object event) {
            postedEvents << event
          }
          void register(Object subscriber) {}
        }
    def context = RequestContext.builder().clientId("client1").build()
    Facilities.addEventBus("client1", eventBus)
    def gateway = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()

    when:
    gateway.beforeAccessor(gateway, new MinimalAccessor(), context)

    then:
    postedEvents.size() == 1

    cleanup:
    Facilities.reset()
  }

  def "executeBehaviorStack executes the behavior chain"() {
    given:
    def gateway = MinimalGateway.builder().baseAccessor(new MinimalAccessor()).build()
    def terminal = new GatewayBehavior(new ObjectMap()) {
          protected <T> AccessorResponse<T> call(Class<T> r, GatewayRequestContext req, GatewayBehavior t) {
            new AccessorResponse<T>()
          }
        }
    def req = GatewayRequestContext.builder().build()
    def execMethod = Gateway.getDeclaredMethod("executeBehaviorStack", Class, GatewayRequestContext, GatewayBehavior)
    execMethod.setAccessible(true)

    when:
    def result = execMethod.invoke(gateway, String, req, terminal)

    then:
    result != null
  }
}
