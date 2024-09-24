package com.mx.path.connect.messaging.remote;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.mx.path.connect.messaging.MessageHeaders;

import io.opentracing.propagation.TextMap;

/**
 * Utility class to extract {@link MessageHeaders}.
 */
public final class MessageHeadersExtractAdaptor implements TextMap {
  private final MessageHeaders messageHeaders;

  /**
   * Build a new {@link MessageHeadersExtractAdaptor} with the specified {@link MessageHeaders}.
   *
   * @param messageHeaders {@link MessageHeaders} to be set
   */
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
