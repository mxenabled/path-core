package com.mx.path.core.context.tracing;

import java.util.Iterator;
import java.util.Map;

import io.opentracing.propagation.TextMap;

public final class HttpHeadersCarrier implements TextMap {
  private final Map<String, String> httpHeaders;

  public HttpHeadersCarrier(Map<String, String> httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  @Override
  public void put(String key, String value) {
    httpHeaders.put(key, value);
  }

  @Override
  public Iterator<Map.Entry<String, String>> iterator() {
    throw new UnsupportedOperationException("HttpHeadersCarrier should be used only with tracer#inject()");
  }
}
