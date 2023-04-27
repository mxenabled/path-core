package com.mx.path.connect.messaging

import com.mx.path.connect.messaging.MessageHeaders

import spock.lang.Specification

class MessageRequestHeadersTest extends Specification {
  MessageHeaders subject

  void setup() {
    subject = MessageHeaders.builder()
        .header("arbitrary", "value")
        .sessionId("session123")
        .build()
  }

  def "put/get"() {
    given:
    subject = new MessageHeaders()

    when:
    subject.put("header", "value")

    then:
    subject.get("header") == "value"
  }

  def "builder"() {
    when:
    subject = MessageHeaders.builder()
        .sessionId("session123")
        .header("arbitrary", "value")
        .build()

    then:
    subject.getSessionId() == "session123"
    subject.get("arbitrary") == "value"
  }

  def "setters/getters"() {
    when:
    subject = new MessageHeaders()

    subject.setSessionId("session123")
    subject.put("arbitrary", "value")

    then:
    subject.getSessionId() == "session123"
    subject.get("arbitrary") == "value"
  }

  def "fromJson"() {
    when:
    def json = "{\"headers\":{\"arbitrary\":\"value\"},\"sessionId\":\"session123\"}"
    subject = MessageHeaders.fromJson(json)

    then:
    subject.get("arbitrary") == "value"
    subject.getSessionId() == "session123"
  }

  def "toJson"() {
    when:
    def json = subject.toJson()

    then:
    json == "{\"headers\":{\"arbitrary\":\"value\"},\"sessionId\":\"session123\"}"
  }
}
