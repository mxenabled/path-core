package com.mx.path.core.context.environment

import io.github.cdimascio.dotenv.Dotenv

import spock.lang.Specification

class EnvironmentTest extends Specification {
  def cleanup() {
  }

  def "test get method"() {
    given:
    def key = "MY_KEY"
    def value = "my value"
    Environment.dotenv = Mock(Dotenv)
    Environment.dotenv().get(key) >> value

    when:
    def result = Environment.get(key)

    then:
    result == value
  }

  def "test get method with default value"() {
    given:
    def key = "MY_KEY"
    def defaultValue = "default value"
    Environment.setDotenv( Mock(Dotenv))
    Environment.dotenv().get(key, defaultValue) >> defaultValue

    when:
    def result = Environment.get(key, defaultValue)

    then:
    result == defaultValue
  }

  def "test reset method"() {
    when:
    Environment.reset()

    then:
    Environment.dotenv == null
    Environment.all == null
    Environment.dotEnv == null
  }
}
