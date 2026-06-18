package com.mx.path.gateway.behavior

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.gateway.accessor.AccessorResponse
import com.mx.path.gateway.context.GatewayRequestContext

import spock.lang.Specification

class GatewayBehaviorTest extends Specification {

  // Minimal concrete behavior that records calls and delegates to callNext
  static class PassThroughBehavior extends GatewayBehavior {
    boolean called = false

    PassThroughBehavior(ObjectMap configs = new ObjectMap()) {
      super(configs)
    }

    @Override
    protected <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
      called = true
      callNext(resultType, request, terminatingBehavior)
    }
  }

  // Terminal behavior that returns a canned response
  static class TerminalBehavior extends GatewayBehavior {
    TerminalBehavior() {
      super(new ObjectMap())
    }

    @Override
    protected <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
      new AccessorResponse<T>().withResult(null)
    }
  }

  def request = GatewayRequestContext.builder().build()

  def "execute delegates to call"() {
    given:
    def behavior = new PassThroughBehavior()
    def terminal = new TerminalBehavior()

    when:
    behavior.execute(String, request, terminal)

    then:
    behavior.called
  }

  def "callNext invokes nextBehavior when set"() {
    given:
    def first = new PassThroughBehavior()
    def second = new PassThroughBehavior()
    def terminal = new TerminalBehavior()
    first.setNextBehavior(second)

    when:
    first.execute(String, request, terminal)

    then:
    first.called
    second.called
  }

  def "callNext falls through to terminatingBehavior when nextBehavior is null"() {
    given:
    def behavior = new PassThroughBehavior()
    def terminal = new TerminalBehavior()

    when:
    def response = behavior.execute(String, request, terminal)

    then:
    response != null
    behavior.getNextBehavior() == null
  }

  def "describe returns class name"() {
    given:
    def behavior = new PassThroughBehavior()

    when:
    def desc = behavior.describe()

    then:
    desc.get("class") == "PassThroughBehavior"
  }

  def "describe includes configurations when non-empty"() {
    given:
    def configs = new ObjectMap()
    configs.put("key", "value")
    def behavior = new PassThroughBehavior(configs)

    when:
    def desc = behavior.describe()

    then:
    desc.containsKey("configurations")
  }

  def "describe omits configurations when empty"() {
    given:
    def behavior = new PassThroughBehavior(new ObjectMap())

    when:
    def desc = behavior.describe()

    then:
    !desc.containsKey("configurations")
  }

  def "BlockBehavior executes block function"() {
    given:
    def called = false
    def behavior = new BlockBehavior({ req ->
      called = true
      new AccessorResponse()
    })
    def terminal = new TerminalBehavior()

    when:
    behavior.execute(String, request, terminal)

    then:
    called
  }

  def "BlockBehavior with configurations"() {
    given:
    def configs = new ObjectMap()
    configs.put("k", "v")
    def behavior = new BlockBehavior(configs, { req -> new AccessorResponse() })

    when:
    def response = behavior.execute(String, request, new TerminalBehavior())

    then:
    response != null
    behavior.getConfigurations().get("k") == "v"
  }

  def "StartBehavior delegates to callNext"() {
    given:
    def start = new StartBehavior()
    def next = new PassThroughBehavior()
    def terminal = new TerminalBehavior()
    start.setNextBehavior(next)

    when:
    start.execute(String, request, terminal)

    then:
    next.called
  }
}
