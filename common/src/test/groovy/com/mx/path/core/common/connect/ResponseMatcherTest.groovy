package com.mx.path.core.common.connect

import com.mx.path.core.common.accessor.UpstreamSystemUnavailable
import com.mx.path.core.common.http.HttpStatus
import com.mx.testing.connect.TestResponse

import spock.lang.Specification

class ResponseMatcherTest extends Specification {

  def "only builds predicate once"() {
    when:
    def subject = ResponseMatcher.builder().status(HttpStatus.ACCEPTED).build()

    def first = subject.predicate()

    then:
    first === subject.predicate()
  }

  def "false when no conditions"() {
    when:
    def subject = ResponseMatcher.builder().build()
    def response = new TestResponse()

    then:
    !subject.test(response)
  }

  def "ands all conditions"() {
    when: "status matches"
    def subject = ResponseMatcher.builder().status(HttpStatus.ACCEPTED).build()
    def response = new TestResponse()
    response.setStatus(HttpStatus.ACCEPTED)

    then:
    subject.test(response)

    when: "status matches, exception does not match"
    subject = ResponseMatcher.builder().status(HttpStatus.ACCEPTED).exception(RuntimeException).build()
    response = new TestResponse()
    response.setStatus(HttpStatus.ACCEPTED)

    then:
    !subject.test(response)

    when: "status and exception both match"
    response.setException(new RuntimeException())

    then:
    subject.test(response)
  }

  def "status condition"() {
    when:
    def subject = ResponseMatcher.builder().status(HttpStatus.ACCEPTED).build()
    def response = new TestResponse()
    response.setStatus(HttpStatus.ACCEPTED)

    then:
    subject.test(response)

    when:
    response.setStatus(HttpStatus.OK)

    then:
    !subject.test(response)

    when: "status is null"
    response.setStatus(null)

    then:
    !subject.test(response)

    and: "when response is null"
    !subject.test(null)
  }

  def "exception condition"() {
    when:
    def subject = ResponseMatcher.builder().exception(RuntimeException).build()
    def response = new TestResponse()
    response.setException(new RuntimeException())

    then:
    subject.test(response)

    when:
    response.setException(new UpstreamSystemUnavailable(""))

    then:
    subject.test(response)

    when:
    response.setException(new InvalidClassException(""))

    then:
    !subject.test(response)

    when:
    response.setException(null)

    then:
    !subject.test(response)
  }

  def "predicate condition"() {
    when:
    def subject = ResponseMatcher.builder().predicate({ t -> false }).build()
    def response = new TestResponse()

    then:
    !subject.test(response)

    when:
    subject = ResponseMatcher.builder().predicate({ t -> true }).build()

    then:
    subject.test(response)
  }
}
