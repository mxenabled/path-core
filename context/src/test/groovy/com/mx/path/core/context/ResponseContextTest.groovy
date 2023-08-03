package com.mx.path.core.context

import spock.lang.Specification

class ResponseContextTest extends Specification {
  def cleanup() {
    ResponseContext.clear()
  }

  def "current"() {
    given:
    def context = ResponseContext.builder().build()

    when:
    context.register()

    then:
    ResponseContext.current() == context
  }

  def "reset"() {
    given:
    def context = ResponseContext.builder().build()

    when:
    context.register()

    then:
    ResponseContext.current() == context

    when:
    ResponseContext.clear()

    then:
    ResponseContext.current() == null
  }

  def "withSelfClearing"() {
    given:
    def context = ResponseContext.builder().build()

    when: "context already set"
    context.register()

    ResponseContext.withSelfClearing({ c ->
      assert context == c, "Must pass current() context"
    })

    then:
    true
  }

  def "builder"() {
    when:
    def subject = ResponseContext.builder().header("h1", "v1")
        .build()

    then:
    subject.getHeaders().get("h1") == "v1"
  }

  def "setters"() {
    given:
    def subject = ResponseContext.builder().build()

    when:
    subject.getHeaders().put("h1", "v1")

    then:
    subject.getHeaders().get("h1") == "v1"
  }
}
