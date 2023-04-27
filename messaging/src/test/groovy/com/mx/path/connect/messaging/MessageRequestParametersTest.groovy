package com.mx.path.connect.messaging

import com.mx.path.connect.messaging.MessageParameters

import spock.lang.Specification

class MessageRequestParametersTest extends Specification {
  MessageParameters subject

  void setup() {
    subject = MessageParameters.builder()
        .id("123")
        .userId("user123")
        .parameter("arbitrary", "value")
        .build()
  }

  def "put/get"() {
    given:
    subject = new MessageParameters()

    when:
    subject.put("param1", "value")

    then:
    subject.get("param1") == "value"
  }

  def "builder"() {
    when:
    subject = MessageParameters.builder()
        .id("123")
        .userId("user123")
        .parameter("arbitrary", "value")
        .build()

    then:
    subject.getId() == "123"
    subject.getUserId() == "user123"
    subject.get("arbitrary") == "value"
  }

  def "getters and setters"() {
    when:
    subject = new MessageParameters()
    subject.setId("123")
    subject.setUserId("123")
    subject.put("arbitrary", "value")

    then:
    subject.getId() == "123"
    subject.getUserId() == "123"
    subject.get("arbitrary") == "value"
  }

  def "fromJson"() {
    when:
    def json = "{\"parameters\":{\"arbitrary\":\"value\"},\"id\":\"123\",\"userId\":\"user123\"}"
    subject = MessageParameters.fromJson(json)

    then:
    subject.get("arbitrary") == "value"
    subject.getId() == "123"
    subject.getUserId() == "user123"
  }

  def "toJson"() {
    when:
    def json = subject.toJson()

    then:
    json == "{\"parameters\":{\"arbitrary\":\"value\"},\"id\":\"123\",\"userId\":\"user123\"}"
  }
}
