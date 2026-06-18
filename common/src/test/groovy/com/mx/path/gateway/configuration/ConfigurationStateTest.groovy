package com.mx.path.gateway.configuration

import spock.lang.Specification

class ConfigurationStateTest extends Specification {

  def setup() {
    ConfigurationState.resetCurrent()
  }

  def "getCurrent returns a singleton instance"() {
    expect:
    ConfigurationState.getCurrent() != null
    ConfigurationState.getCurrent().is(ConfigurationState.getCurrent())
  }

  def "field sets and retrieves the current field name"() {
    given:
    def state = ConfigurationState.getCurrent()

    when:
    state.field("host")

    then:
    state.field() == "host"
  }

  def "pushLevel and popLevel manage the state stack"() {
    given:
    def state = ConfigurationState.getCurrent()

    when:
    state.pushLevel("client")
    state.pushLevel("accessor")

    then:
    state.currentState() == "client.accessor"

    when:
    state.popLevel()

    then:
    state.currentState() == "client"
  }

  def "currentState returns empty string when stack is empty"() {
    given:
    def state = ConfigurationState.getCurrent()

    expect:
    state.currentState() == ""
  }

  def "withField(Runnable) runs the runnable and resets field"() {
    given:
    def state = ConfigurationState.getCurrent()
    def executed = false

    when:
    state.withField("host") { executed = true }

    then:
    executed
    state.field() == null
  }

  def "withField(Runnable) rethrows ConfigurationError unchanged"() {
    given:
    def state = ConfigurationState.getCurrent()

    when:
    state.withField("host") {
      throw new ConfigurationError("bad config", "host")
    }

    then:
    thrown(ConfigurationError)
  }

  def "withField(Runnable) wraps other exceptions in ConfigurationError"() {
    given:
    def state = ConfigurationState.getCurrent()

    when:
    state.withField("host") {
      throw new RuntimeException("unexpected")
    }

    then:
    thrown(ConfigurationError)
  }

  def "withField(Supplier) returns supplier value and resets field"() {
    given:
    def state = ConfigurationState.getCurrent()

    when:
    def result = state.withField("port", { -> "8080" } as java.util.function.Supplier)

    then:
    result == "8080"
    state.field() == null
  }

  def "withField(Supplier) rethrows ConfigurationError"() {
    given:
    def state = ConfigurationState.getCurrent()

    when:
    state.withField("port", { -> throw new ConfigurationError("invalid", "port") } as java.util.function.Supplier)

    then:
    thrown(ConfigurationError)
  }

  def "withLevel(Runnable) pushes and pops level"() {
    given:
    def state = ConfigurationState.getCurrent()
    def capturedState = ""

    when:
    state.withLevel("client") {
      capturedState = state.currentState()
    }

    then:
    capturedState == "client"
    state.currentState() == ""
  }

  def "withLevel(Runnable) pops level even on exception"() {
    given:
    def state = ConfigurationState.getCurrent()

    when:
    state.withLevel("client") {
      throw new RuntimeException("error")
    }

    then:
    thrown(ConfigurationError)
    state.currentState() == ""
  }

  def "withLevel(Supplier) returns value and pops level"() {
    given:
    def state = ConfigurationState.getCurrent()

    when:
    def result = state.withLevel("client", { -> "done" } as java.util.function.Supplier)

    then:
    result == "done"
    state.currentState() == ""
  }

  def "withLevel(Supplier) rethrows ConfigurationError"() {
    given:
    def state = ConfigurationState.getCurrent()

    when:
    state.withLevel("client", { -> throw new ConfigurationError("oops", state) } as java.util.function.Supplier)

    then:
    thrown(ConfigurationError)
  }
}
