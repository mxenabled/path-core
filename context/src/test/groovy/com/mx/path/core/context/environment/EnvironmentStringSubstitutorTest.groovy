package com.mx.path.core.context.environment

import org.junit.jupiter.api.extension.ExtendWith

import spock.lang.Specification
import uk.org.webcompere.systemstubs.SystemStubs
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables
import uk.org.webcompere.systemstubs.jupiter.SystemStub
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension

@ExtendWith(SystemStubsExtension.class)
class EnvironmentStringSubstitutorTest extends Specification {

  def setup() {
    Environment.reset()
  }

  def cleanup() {
    Environment.reset()
  }

  def "substitutes environment variables"() {
    given:
    def key = "SYS_ENV_KEY"
    def value = "my value"
    def targetString = "SYS_ENV_KEY == \${env:SYS_ENV_KEY}"

    when:
    def result = ""
    SystemStubs.withEnvironmentVariable(key, value).execute({
      assert(System.getenv("SYS_ENV_KEY") == "my value")
      result = EnvironmentStringSubstitutor.replace(targetString)
    })

    then:
    result == "SYS_ENV_KEY == my value"
  }

  def "substitutes environment variables from .env"() {
    given:
    Environment.setDotEnvLocation("src/test/resources/test.env")
    def targetString = "DOT_ENV_KEY == \${env:DOT_ENV_KEY}"

    when:
    def result = EnvironmentStringSubstitutor.replace(targetString)

    then:
    result == "DOT_ENV_KEY == dot env value"
  }

  def "leaves substitutions with no match"() {
    given:
    def targetString = "SOME_ENV_KEY == \${env:SOME_ENV_KEY}"

    when:
    def result = EnvironmentStringSubstitutor.replace(targetString)

    then:
    result == "SOME_ENV_KEY == \${env:SOME_ENV_KEY}"
  }
}
