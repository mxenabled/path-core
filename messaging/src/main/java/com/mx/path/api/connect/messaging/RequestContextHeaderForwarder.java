package com.mx.path.api.connect.messaging;

import com.mx.path.model.context.RequestContext;

/**
 * Facilitates injecting and extracting fields between MessageHeaders and RequestContext.
 */
public final class RequestContextHeaderForwarder {
  static final String ORIGINATING_IP_HEADER = "x-forwarded-for";
  static final String SESSION_TRACE_ID_HEADER = "session-trace-id";
  static final String FEATURE_HEADER = "feature";
  static final String DEVICE_TRACE_ID_HEADER = "device-trace-id";
  static final String CLIENT_GUID_HEADER = "client-guid";

  /**
   * Populates MessageHeaders with values from the supplied RequestContext.
   *
   * @param requestContext The RequestContext that will be used as the header-value source.
   * @param messageHeaders The MessageHeaders that will be used as the header-value destination.
   */
  public void injectIntoMessageHeaders(RequestContext requestContext, MessageHeaders messageHeaders) {
    if (requestContext == null || messageHeaders == null) {
      return;
    }

    messageHeaders.getHeaders().put(ORIGINATING_IP_HEADER, requestContext.getOriginatingIP());
    messageHeaders.getHeaders().put(SESSION_TRACE_ID_HEADER, requestContext.getSessionTraceId());
    messageHeaders.getHeaders().put(FEATURE_HEADER, requestContext.getFeature());
    messageHeaders.getHeaders().put(DEVICE_TRACE_ID_HEADER, requestContext.getDeviceTraceId());
    messageHeaders.getHeaders().put(CLIENT_GUID_HEADER, requestContext.getClientGuid());
  }

  /**
   * Populates the RequestContext with values from the supplied MessageHeaders.
   *
   * @param requestContext The RequestContext that will be used as the header-value destination.
   * @param messageHeaders The MessageHeaders that will be used as the header-value source.
   */
  public void extractFromMessageHeaders(RequestContext requestContext, MessageHeaders messageHeaders) {
    if (requestContext == null || messageHeaders == null) {
      return;
    }

    requestContext.setOriginatingIP(messageHeaders.get(ORIGINATING_IP_HEADER));
    requestContext.setSessionTraceId(messageHeaders.get(SESSION_TRACE_ID_HEADER));
    requestContext.setFeature(messageHeaders.get(FEATURE_HEADER));
    requestContext.setDeviceTraceId(messageHeaders.get(DEVICE_TRACE_ID_HEADER));
    requestContext.setClientGuid(messageHeaders.get(CLIENT_GUID_HEADER));
  }
}
