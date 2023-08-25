package com.mx.path.connect.messaging

import com.mx.path.core.common.messaging.MessageStatus
import com.mx.testing.RemoteAccount

import spock.lang.Specification

class MessageResponseTest extends Specification {
  MessageResponse subject
  MessageRequest request
  MessageHeaders headers

  void setup() {
    headers = MessageHeaders.builder()
        .sessionId("session123")
        .header("arbitrary", "value")
        .build()

    subject = MessageResponse.builder()
        .messageHeaders(headers)
        .status(MessageStatus.SUCCESS)
        .build()

    request = MessageRequest.builder().build()
  }

  def "builder"() {
    when:
    subject = MessageResponse.builder()
        .status(MessageStatus.SUCCESS)
        .body("body")
        .request(request)
        .error("Something bad happened")
        .build()

    then:
    subject.getBody() == "body"
    subject.getStatus() == MessageStatus.SUCCESS
    subject.getError() == "Something bad happened"
    subject.getRequest() == request
  }

  def "getters and setters"() {
    when:
    subject = new MessageResponse()
    subject.setBody("body")
    subject.setStatus(MessageStatus.SUCCESS)

    then:
    subject.getBody() == "body"
    subject.getStatus() == MessageStatus.SUCCESS
  }

  def "fromJson"() {
    when:
    def json = "{\"messageHeaders\":{\"headers\":{\"arbitrary\":\"value\"},\"sessionId\":\"session123\"},\"status\":\"SUCCESS\"}"
    subject = MessageResponse.fromJson(json)

    then:
    subject.getMessageHeaders().get("arbitrary") == "value"
    subject.getStatus() == MessageStatus.SUCCESS
  }

  def "toJson"() {
    when:
    def json = subject.toJson()

    then:
    json == "{\"messageHeaders\":{\"headers\":{\"arbitrary\":\"value\"},\"sessionId\":\"session123\"},\"status\":\"SUCCESS\",\"exception\":{}}"
  }

  def "getBodyAs"() {
    given:
    def body = new RemoteAccount()
    body.setId("account123")

    when:
    subject.setBody(body)

    then:
    subject.getBodyAs(RemoteAccount).getId() == body.getId()
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
    subject = MessageResponse.builder().body("body").build()

    then:
    subject.getBody() == "body"
  }

  def "builder.body with primitive"() {
    when:
    subject = MessageResponse.builder().body(12).build()

    then:
    subject.getBodyAs(Integer.class) == 12
  }

  def "builder.body with object"() {
    given:
    def body = Arrays.asList("elem1", "elem2")
    subject = MessageResponse.builder().body(body).build()

    when:
    def result = subject.getBodyAs(ArrayList.class)

    then:
    result.size() == 2
  }
}
