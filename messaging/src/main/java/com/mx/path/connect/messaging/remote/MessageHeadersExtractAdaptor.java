package com.mx.path.connect.messaging.remote;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.mx.path.connect.messaging.MessageHeaders;

import io.opentracing.propagation.TextMap;

public final class MessageHeadersExtractAdaptor implements TextMap {
  private final MessageHeaders messageHeaders;

  public MessageHeadersExtractAdaptor(MessageHeaders messageHeaders) {
    this.messageHeaders = messageHeaders;
  }

  @Override
  public Iterator<Map.Entry<String, String>> iterator() {
    if (messageHeaders == null) {
      return Collections.emptyIterator();
    }
    return messageHeaders.getHeaders().entrySet().iterator();
  }

  @Override
  public void put(String key, String value) {
    throw new UnsupportedOperationException("MessageHeaderExtractAdaptor should be used only with tracer#extract()");
  }
}
