package com.mx.path.gateway.accessor

import com.mx.path.core.common.accessor.PathResponseStatus
import com.mx.path.gateway.accessor.AccessorResponse

import spock.lang.Specification

class AccessorResponseTest extends Specification {

  def "withResult sets result and returns self"() {
    given:
    def response = new AccessorResponse<String>()

    when:
    def returned = response.withResult("hello")

    then:
    response.getResult() == "hello"
    returned.is(response)
  }

  def "withStatus sets status and returns self"() {
    given:
    def response = new AccessorResponse<String>()

    when:
    def returned = response.withStatus(PathResponseStatus.OK)

    then:
    response.getStatus() == PathResponseStatus.OK
    returned.is(response)
  }

  def "withException sets exception and returns self"() {
    given:
    def response = new AccessorResponse<String>()
    def ex = new RuntimeException("boom")

    when:
    def returned = response.withException(ex)

    then:
    response.getException().is(ex)
    returned.is(response)
  }

  def "withHeader adds header and returns self"() {
    given:
    def response = new AccessorResponse<String>()

    when:
    def returned = response.withHeader("X-Custom", "value")

    then:
    response.getHeaders()["X-Custom"] == "value"
    returned.is(response)
  }

  def "withMetadata adds metadata and returns self"() {
    given:
    def response = new AccessorResponse<String>()

    when:
    def returned = response.withMetadata("key", "data")

    then:
    response.getMetadata().get("key") == "data"
    returned.is(response)
  }

  def "chaining multiple withs"() {
    when:
    def response = new AccessorResponse<String>()
        .withResult("result")
        .withStatus(PathResponseStatus.OK)
        .withHeader("X-A", "1")
        .withMetadata("m", "v")

    then:
    response.getResult() == "result"
    response.getStatus() == PathResponseStatus.OK
    response.getHeaders()["X-A"] == "1"
    response.getMetadata().get("m") == "v"
  }

  def "default constructor leaves fields null"() {
    when:
    def response = new AccessorResponse<String>()

    then:
    response.getResult() == null
    response.getStatus() == null
    response.getException() == null
    response.getHeaders().isEmpty()
  }
}
