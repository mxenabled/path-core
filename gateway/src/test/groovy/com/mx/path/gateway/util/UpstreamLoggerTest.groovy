package com.mx.path.gateway.util

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import com.mx.common.collections.MultiValueMap
import com.mx.common.collections.SingleValueMap
import com.mx.common.http.HttpStatus
import com.mx.path.gateway.net.Request
import com.mx.path.gateway.net.Response
import com.mx.path.model.context.RequestContext
import com.mx.path.model.context.Session
import com.mx.testing.WithSessionRepository

import org.slf4j.Logger
import org.slf4j.MDC

import spock.lang.Specification

class UpstreamLoggerTest extends Specification implements WithSessionRepository {
  Request request
  Response response
  UpstreamLogger subject
  Logger logger

  def setup() {
    logger = mock(Logger.class)
    UpstreamLogger.setLogger(logger)
    Session.createSession()

    subject = new UpstreamLogger()
    request = new Request()
    response = new Response(request)
    Session.current().setUserId("user123")
    RequestContext.builder()
        .clientId("client-id")
        .path("/testing")
        .deviceTraceId("device123")
        .sessionTraceId("ebebebe")
        .userId("user-id")
        .originatingIP("10.10.10.1")
        .feature("feature")
        .build()
        .register()
    request
        .withBaseUrl("http://localhost")
        .withPath("/testing")
        .withMethod("GET")
        .withBody("hi")
    response
        .withBody("hi")
        .withStatus(HttpStatus.OK)
        .withDuration(100)
        .withHeaders(new MultiValueMap<String, String>())
  }

  def cleanup() {
    UpstreamLogger.resetLogger()
    MDC.clear()
    RequestContext.clear()
  }

  def "interacts with the logger"() {
    when:
    subject.logRequest(response)
    verify(logger).info("Upstream Request")

    then:
    MDC.get("api_request_payload") == null
    MDC.get("device_trace_id") == null
  }

  def "with empty request context and response"() {
    given:
    request = new Request()
    response = new Response(request)

    RequestContext.builder()
        .build()
        .register()

    // Minimum request values
    request
        .withBaseUrl("http://localhost")
        .withPath("/testing")
        .withMethod("GET")

    when:
    subject.logRequest(response)
    verify(logger).info("Upstream Request")

    then:
    MDC.get("api_request_payload") == null
  }

  def "with no active session"() {
    given:
    request = new Request()
    response = new Response(request)

    RequestContext.builder()
        .build()
        .register()

    Session.clearSession()

    // Minimum request values
    request
        .withBaseUrl("http://localhost")
        .withPath("/testing")
        .withMethod("GET")

    when:
    subject.logRequest(response)
    verify(logger).info("Upstream Request")

    then:
    noExceptionThrown()
  }
}
