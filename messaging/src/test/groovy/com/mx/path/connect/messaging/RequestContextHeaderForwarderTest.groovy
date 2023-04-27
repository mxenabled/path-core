package com.mx.path.connect.messaging

import com.mx.path.connect.messaging.MessageHeaders
import com.mx.path.connect.messaging.RequestContextHeaderForwarder
import com.mx.path.core.context.RequestContext

import spock.lang.Specification

class RequestContextHeaderForwarderTest extends Specification {
  RequestContextHeaderForwarder subject

  def setup() {
    subject = new RequestContextHeaderForwarder()
  }

  def "injectIntoMessageHeaders"() {
    given:
    def headers = MessageHeaders.builder().headers(new HashMap<String, String>()).build()
    def requestContext = RequestContext.builder()
        .originatingIP("originatingIP")
        .sessionTraceId("sessionTraceId")
        .feature("feature")
        .deviceTraceId("deviceTraceId")
        .clientGuid("clientGuid")
        .build()

    when:
    subject.injectIntoMessageHeaders(requestContext, headers)

    then:
    headers.getHeaders().get(RequestContextHeaderForwarder.ORIGINATING_IP_HEADER) == "originatingIP"
    headers.getHeaders().get(RequestContextHeaderForwarder.SESSION_TRACE_ID_HEADER) == "sessionTraceId"
    headers.getHeaders().get(RequestContextHeaderForwarder.FEATURE_HEADER) == "feature"
    headers.getHeaders().get(RequestContextHeaderForwarder.DEVICE_TRACE_ID_HEADER) == "deviceTraceId"
    headers.getHeaders().get(RequestContextHeaderForwarder.CLIENT_GUID_HEADER) == "clientGuid"
  }

  def "extractFromMessageHeaders"() {
    given:
    def headers = MessageHeaders.builder().headers(new HashMap<String, String>().tap {
      put(RequestContextHeaderForwarder.ORIGINATING_IP_HEADER, "originatingIP")
      put(RequestContextHeaderForwarder.SESSION_TRACE_ID_HEADER, "sessionTraceId")
      put(RequestContextHeaderForwarder.FEATURE_HEADER, "feature")
      put(RequestContextHeaderForwarder.DEVICE_TRACE_ID_HEADER, "deviceTraceId")
      put(RequestContextHeaderForwarder.CLIENT_GUID_HEADER, "clientGuid")
    }).build()
    def requestContext = RequestContext.builder().build()

    when:
    subject.extractFromMessageHeaders(requestContext, headers)

    then:
    requestContext.originatingIP == "originatingIP"
    requestContext.sessionTraceId == "sessionTraceId"
    requestContext.feature == "feature"
    requestContext.deviceTraceId == "deviceTraceId"
    requestContext.clientGuid == "clientGuid"
  }
}
