package com.mx.path.core.context.environment

import org.apache.commons.text.EnvironmentStringSubstitutor
import org.junit.jupiter.api.extension.ExtendWith

import spock.lang.Specification
import uk.org.webcompere.systemstubs.SystemStubs
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension

@ExtendWith(SystemStubsExtension.class)
class EnvironmentStringSubstitutorTest extends Specification {

  def setup() {
    Environment.reset()
  }

  def cleanup() {
    Environment.reset()
  }

  def "should return the input string if it is null"() {
    when:
    def result = EnvironmentStringSubstitutor.replace(null)

    then:
    result == null
  }

  def "substitutes environment variables"() {
    given:
    def key = "SYS_ENV_KEY"
    def value = "my value"
    def targetString = "SYS_ENV_KEY == \${env:SYS_ENV_KEY}"

    when:
    def result = ""
    SystemStubs.withEnvironmentVariable(key, value).execute({
      assert(System.getenv(key) == value) // Ensure SystemStubs is working
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

  def "takes provided default when no match"() {
    given:
    def targetString = "SOME_ENV_KEY == \${env:SOME_ENV_KEY:-default value}"

    when:
    def result = EnvironmentStringSubstitutor.replace(targetString)

    then:
    result == "SOME_ENV_KEY == default value"
  }

  def "substitutes when provided default and match in env file"() {
    given:
    Environment.setDotEnvLocation("src/test/resources/test.env")
    def targetString = "DOT_ENV_KEY == \${env:DOT_ENV_KEY:-default value}"

    when:
    def result = EnvironmentStringSubstitutor.replace(targetString)

    then:
    result == "DOT_ENV_KEY == dot env value"
  }

  def "substitutes when default is provided and match in system environment variables"() {
    given:
    def key = "SYS_ENV_KEY"
    def value = "my value"
    def targetString = "SYS_ENV_KEY == \${env:SYS_ENV_KEY:-default value}"

    when:
    def result = ""
    SystemStubs.withEnvironmentVariable(key, value).execute({
      assert(System.getenv(key) == value) // Ensure SystemStubs is working
      result = EnvironmentStringSubstitutor.replace(targetString)
    })

    then:
    result == "SYS_ENV_KEY == my value"
  }

  def "leaves unmatched prefixes"() {
    given:
    def targetString = "decoded string == \${base64Decoder:SGVsbG9Xb3JsZCE=}"

    when:
    def result = EnvironmentStringSubstitutor.replace(targetString)

    then:
    result == "decoded string == HelloWorld!"
  }
}
