package com.mx.path.connect.messaging

import com.mx.path.connect.messaging.MessageHeaders
import com.mx.path.connect.messaging.MessageParameters
import com.mx.path.connect.messaging.MessageRequest

import spock.lang.Specification

class MessageRequestTest extends Specification {
  MessageRequest subject
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

    subject = MessageRequest.builder()
        .messageHeaders(headers)
        .messageParameters(parameters)
        .build()
  }

  def "builder"() {
    when:
    subject = MessageRequest.builder()
        .messageHeaders(headers)
        .messageParameters(parameters)
        .body("body")
        .build()

    then:
    subject.getBody() == "body"
    subject.getMessageHeaders() == headers
    subject.getMessageParameters() == parameters
  }

  def "getters and setters"() {
    when:
    subject = new MessageRequest()
    subject.setBody("body")
    subject.setMessageHeaders(headers)
    subject.setMessageParameters(parameters)
    subject.setStartNano(0)
    subject.setEndNano(0)
    subject.setModel("Account")

    then:
    subject.getBody() == "body"
    subject.getMessageHeaders() == headers
    subject.getMessageParameters() == parameters
    subject.getStartNano() == 0
    subject.getEndNano() == 0
    subject.getModel() == "Account"
  }

  def "toJson"() {
    when:
    def json = subject.toJson()

    then:
    json == "{\"messageHeaders\":{\"headers\":{\"arbitrary\":\"value\"},\"sessionId\":\"session123\"},\"messageParameters\":{\"parameters\":{\"param1\":\"value\"},\"id\":\"123\",\"userId\":\"user123\"}}"
  }

  def "fromJson"() {
    given:
    def json = "{\"messageHeaders\":{\"headers\":{\"arbitrary\":\"value\"},\"sessionId\":\"session123\"},\"messageParameters\":{\"parameters\":{\"param1\":\"value\"},\"id\":\"123\",\"userId\":\"user123\"}}"

    when:
    subject = MessageRequest.fromJson(json)

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
    subject = MessageRequest.builder().body("body").build()

    then:
    subject.getBody() == "body"
  }

  def "builder.body with primitive"() {
    when:
    subject = MessageRequest.builder().body(12).build()

    then:
    subject.getBodyAs(Integer.class) == 12
  }

  def "builder.body with object"() {
    given:
    def body = Arrays.asList("elem1", "elem2")
    subject = MessageRequest.builder().body(body).build()

    when:
    def result = subject.getBodyAs(ArrayList.class)

    then:
    result.size() == 2
  }

  def "start and finish"() {
    when:
    subject.start()
    Thread.sleep(10)
    def duration = subject.getDuration()

    then:
    duration > 9
  }
}
