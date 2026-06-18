package com.mx.path.gateway.configuration

import spock.lang.Specification

class ConfigurationErrorTest extends Specification {

  def "message and field constructor"() {
    given:
    def ex = new ConfigurationError("missing required", "host")

    expect:
    ex.getMessage().contains("missing required")
    ex.getMessage().contains("host")
  }

  def "message, field and cause constructor"() {
    given:
    def cause = new RuntimeException("root")
    def ex = new ConfigurationError("invalid value", "port", cause)

    expect:
    ex.getCause().is(cause)
    ex.getMessage().contains("port")
  }

  def "message and ConfigurationState constructor"() {
    given:
    ConfigurationState.resetCurrent()
    def state = ConfigurationState.getCurrent()
    def ex = new ConfigurationError("bad config", state)

    expect:
    ex.getMessage().contains("bad config")
  }

  def "message, ConfigurationState and cause constructor"() {
    given:
    ConfigurationState.resetCurrent()
    def state = ConfigurationState.getCurrent()
    def cause = new RuntimeException("root")
    def ex = new ConfigurationError("bad config", state, cause)

    expect:
    ex.getCause().is(cause)
    ex.getMessage().contains("bad config")
  }
}
