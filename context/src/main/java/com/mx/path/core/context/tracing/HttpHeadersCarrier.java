package com.mx.path.core.context.tracing;

import java.util.Iterator;
import java.util.Map;

import io.opentracing.propagation.TextMap;

/**
 * Container class for http headers.
 */
public final class HttpHeadersCarrier implements TextMap {
  private final Map<String, String> httpHeaders;

  /**
   * Build new {@link HttpHeadersCarrier} instance with given headers.
   *
   * @param httpHeaders headers to set
   */
  public HttpHeadersCarrier(Map<String, String> httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  /**
   * Add new key and value pair to headers map.
   *
   * @param key key
   * @param value value
   *
   */
  @Override
  public void put(String key, String value) {
    httpHeaders.put(key, value);
  }

  /**
   * Override to make {@link #iterator()} unsupported.
   *
   * @return throws {@link UnsupportedOperationException}
   */
  @Override
  public Iterator<Map.Entry<String, String>> iterator() {
    throw new UnsupportedOperationException("HttpHeadersCarrier should be used only with tracer#inject()");
  }
}
