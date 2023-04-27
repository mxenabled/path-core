package com.mx.path.connect.messaging.remote

import static org.mockito.Mockito.mock

import com.mx.path.connect.messaging.MessageEvent
import com.mx.path.connect.messaging.MessageHeaders
import com.mx.path.connect.messaging.MessageParameters
import com.mx.path.connect.messaging.MessageRequest
import com.mx.path.connect.messaging.MessageResponse
import com.mx.path.connect.messaging.remote.RemoteLogger
import com.mx.path.core.common.messaging.MessageStatus
import com.mx.path.core.context.RequestContext

import org.slf4j.Logger
import org.slf4j.MDC

import spock.lang.Specification

class RemoteLoggerTest extends Specification {
  RemoteLogger subject
  Logger logger
  MessageRequest request
  MessageResponse response
  MessageEvent event

  void setup() {
    logger = mock(Logger)
    RemoteLogger.setLogger(logger)
    subject = new RemoteLogger()
    request = MessageRequest.builder().build()
    response = MessageResponse.builder().request(request).build()

    RequestContext.builder()
        .build()
        .register()
  }

  def cleanup() {
    RequestContext.clear()
    RemoteLogger.resetLogger()
    MDC.clear()
  }

  def "log blank response"() {
    when:
    subject.logRequest(response)

    then:
    MDC.get("api_call_payload") == null
  }

  def "log response without request"() {
    given:
    RequestContext.builder()
        .userId("userId")
        .userGuid("userGuid")
        .clientId("clientId")
        .clientGuid("clientGuid")
        .originatingIP("127.0.0.1")
        .sessionTraceId("sessionTraceId")
        .deviceTraceId("deviceTraceId")
        .feature("feature")
        .build()
        .register()

    request.setBody("body")
    request.setChannel("channel")
    request.setModel("model")
    request.setOperation("get")
    request.start()
    request.finish()
    request.setMessageHeaders(MessageHeaders.builder()
        .header("X-B3-TraceId", "trace")
        .header("X-B3-SpanId", "span")
        .sessionId("session")
        .build())
    request.setMessageParameters(MessageParameters.builder()
        .id("id")
        .userId("userId")
        .parameter("parameter", "value")
        .build())

    response.setBody("body")
    response.setStatus(MessageStatus.SUCCESS)
    response.setError("error")
    response.setException(new Exception())
    response.setMessageHeaders(MessageHeaders.builder()
        .header("response", "header")
        .build())

    when:
    subject.logRequest(response)

    then:
    noExceptionThrown()
  }
}
