package com.mx.path.gateway.util

import com.google.gson.Gson
import com.mx.common.http.HttpStatus

import spock.lang.Specification

class MdxApiExceptionTest extends Specification {
  Exception cause

  def setup() {
    cause = new Exception()
  }

  def "with status"() {
    when: "invoke constructor with status"
    def subject = new MdxApiException(HttpStatus.UNPROCESSABLE_ENTITY)

    then: "status is saved, all else null"
    subject.getCause() == null
    subject.getStatus().toHttpStatus() == HttpStatus.UNPROCESSABLE_ENTITY
    subject.getMessage() == null
    subject.getUserMessage() == null
    subject.getClientId() == null
    subject.getEndpointKey() == null
    subject.getErrorKey() == null
  }

  def "without clientId, userMessage, endpointKey or errorKey"() {
    when: "invoke constructor without clientId, endpointKey or errorKey"
    def subject = new MdxApiException("the message", HttpStatus.BAD_REQUEST, false, cause)

    then: "given field values are saved and reporting is turned on"
    subject.getMessage() == "the message"
    subject.getStatus().toHttpStatus() == HttpStatus.BAD_REQUEST
    subject.getCause() == cause
    subject.getEndpointKey() == null
    subject.getErrorKey() == null
    subject.getClientId() == null
    subject.getUserMessage() == null
    !subject.shouldReport()
  }

  def "with all attributes"() {
    when: "invoke constructor with all attributes"
    def subject = new MdxApiException(
        "the message",
        "testClient",
        HttpStatus.BAD_REQUEST,
        "feature",
        "errorCode",
        true,
        cause)

    then: "given field values are saved"
    subject.getMessage() == "the message"
    subject.cause == cause
    subject.shouldReport()
    subject.getStatus().toHttpStatus() == HttpStatus.BAD_REQUEST
    subject.getEndpointKey() == "feature"
    subject.getClientId() == "testClient"
    subject.getErrorKey() == "errorCode"

    then: "userMessage and reason are left null"
    subject.getUserMessage() == null
    subject.getReason() == null
  }

  def "withCode()"() {
    given:
    def subject = new MdxApiException(HttpStatus.OK)

    when:
    subject.withCode("4001")

    then:
    subject.getCode() == "4001"
  }

  def "withReason()"() {
    given:
    def subject = new MdxApiException(HttpStatus.OK)

    when:
    subject.withReason("because")

    then:
    subject.getReason() == "because"
  }

  def "without endpointKey and errorKey"() {
    given:
    def subject = new MdxApiException("Internal err msg", "External err msg", HttpStatus.BAD_REQUEST, true, new Exception("some throwable"))

    expect:
    subject.getMessage() == "Internal err msg"
    subject.getUserMessage() == "External err msg"
    subject.getStatus().toHttpStatus() == HttpStatus.BAD_REQUEST
    subject.shouldReport()
    subject.getCause().getMessage() == "some throwable"
    subject.getErrorKey() == null
    subject.getEndpointKey() == null
    subject.getClientId() == null
  }

  def "with headers"() {
    given:
    def subject = new MdxApiException(HttpStatus.OK)
        .withHeader("header1", "value1")
        .withHeader("header2", "value2")

    expect:
    subject.headers.size() == 2
    subject.headers.get("header1") == "value1"
    subject.headers.get("header2") == "value2"
  }

  def "setMessage"() {
    when:
    def subject = new MdxApiException("Original message", "User message", HttpStatus.OK, false, null)

    then:
    subject.getMessage() == "Original message"

    when:
    subject.setMessage("New message")

    then:
    subject.getMessage() == "New message"
  }

  def "serializes"() {
    given:
    def ex = new MdxApiException("message1", "client1", HttpStatus.BAD_REQUEST, "title1", "endpoint1", "errorKey1", true, null)

    when:
    def json = new Gson().toJson(ex)

    then:
    verifyAll(json) {
      contains("message1")
      contains("client1")
      contains("title1")
      contains("endpoint1")
      contains("errorKey1")
    }
  }
}
