package com.mx.path.connect.messaging

import com.mx.path.connect.messaging.MessageEvent
import com.mx.path.connect.messaging.MessageHeaders
import com.mx.path.connect.messaging.MessageParameters

import spock.lang.Specification

class MessageEventTest extends Specification {
  MessageEvent subject
  MessageHeaders headers
  MessageParameters parameters

  def setup() {
    headers = MessageHeaders.builder()
        .header("arbitrary", "value")
        .sessionId("session123")
        .build()

    parameters = MessageParameters.builder()
        .parameter("param1", "value")
        .userId("user123")
        .id("123")
        .build()

    subject = MessageEvent.builder()
        .event('$#!^ happened')
        .messageHeaders(headers)
        .messageParameters(parameters)
        .build()
  }

  def "builder"() {
    when:
    subject = MessageEvent.builder()
        .messageHeaders(headers)
        .messageParameters(parameters)
        .body("body")
        .event('$#!^ happened')
        .build()

    then:
    subject.getBody() == "body"
    subject.getEvent() == '$#!^ happened'
    subject.getMessageHeaders() == headers
    subject.getMessageParameters() == parameters
  }

  def "getters and setters"() {
    when:
    subject = new MessageEvent()
    subject.setBody("body")
    subject.setEvent('$#!^ happened')
    subject.setMessageHeaders(headers)
    subject.setMessageParameters(parameters)

    then:
    subject.getBody() == "body"
    subject.getEvent() == '$#!^ happened'
    subject.getMessageHeaders() == headers
    subject.getMessageParameters() == parameters
  }

  def "toJson"() {
    when:
    def json = subject.toJson()

    then:
    json == "{\"event\":\"\$#!^ happened\",\"messageHeaders\":{\"headers\":{\"arbitrary\":\"value\"},\"sessionId\":\"session123\"},\"messageParameters\":{\"parameters\":{\"param1\":\"value\"},\"id\":\"123\",\"userId\":\"user123\"}}"
  }

  def "fromJson"() {
    given:
    def json = "{\"messageHeaders\":{\"headers\":{\"arbitrary\":\"value\"},\"sessionId\":\"session123\"},\"messageParameters\":{\"parameters\":{\"param1\":\"value\"},\"id\":\"123\",\"userId\":\"user123\"}}"

    when:
    subject = MessageEvent.fromJson(json)

    then:
    subject.getMessageParameters().get("param1") == "value"
  }

  def "setBody with String"() {
    when:
    subject.setBody("body")

    then:
    subject.getBody() == "body"
  }

  def "setBody with primitive"() {
    when:
    subject.setBody(12)

    then:
    subject.getBodyAs(Integer.class) == 12
  }

  def "setBody with object"() {
    given:
    def body = Arrays.asList("elem1", "elem2")
    subject.setBody(body)

    when:
    def result = subject.getBodyAs(ArrayList.class)

    then:
    result.size() == 2
  }

  def "builder.body with String"() {
    when:
    subject = MessageEvent.builder().body("body").build()

    then:
    subject.getBody() == "body"
  }

  def "builder.body with primitive"() {
    when:
    subject = MessageEvent.builder().body(12).build()

    then:
    subject.getBodyAs(Integer.class) == 12
  }

  def "builder.body with object"() {
    given:
    def body = Arrays.asList("elem1", "elem2")
    subject = MessageEvent.builder().body(body).build()

    when:
    def result = subject.getBodyAs(ArrayList.class)

    then:
    result.size() == 2
  }
}
