package com.mx.path.connect.messaging.remote;

import java.util.Iterator;
import java.util.Map;

import com.mx.path.connect.messaging.MessageHeaders;

import io.opentracing.propagation.TextMap;

public final class MessageHeadersCarrier implements TextMap {
  private final MessageHeaders messageHeaders;

  public MessageHeadersCarrier(MessageHeaders messageHeaders) {
    this.messageHeaders = messageHeaders;
  }

  @Override
  public void put(String key, String value) {
    if (messageHeaders != null) {
      messageHeaders.put(key, value);
    }
  }

  @Override
  public Iterator<Map.Entry<String, String>> iterator() {
    throw new UnsupportedOperationException("HttpHeadersCarrier should be used only with tracer#inject()");
  }
}
