package com.mx.path.gateway.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.mx.common.lang.Strings;

/**
 * Creates and manages request processors.
 * <p>
 * Requests with the same base_url and feature use the same request
 * processor. Request processors are expected to be thread safe and stateless.
 */
public class RequestProcessorFactory {
  /**
   * Cache request processors by baseUrl and feature.
   *
   * This is being done to avoid excessive object creation. Stored based on hashcode of baseUrl and feature because
   * because this is how the critical connection attributes differ as well.
   */
  private static Map<Long, RequestProcessor> processors = new HashMap<>();

  public static RequestProcessor forRequest(Request request) {
    long requestHashCode = getRequestHashCode(request);
    if (!processors.containsKey(requestHashCode)) {
      synchronized (RequestProcessorFactory.class) {
        processors.put(requestHashCode, HttpClientRequestProcessor.forRequest(request));
      }
    }

    return processors.get(requestHashCode);
  }

  private static long getRequestHashCode(Request request) {
    return (Strings.isBlank(request.getBaseUrl()) ? 1 : request.getBaseUrl().hashCode()) * (Objects.isNull(request.getFeature()) ? 1 : request.getFeature().hashCode());
  }

  public static void resetProcessors() {
    processors.clear();
  }

  /**
   * Primarily for testing. Use to set a mock processor for a given request.
   *
   * @param request
   * @param processor
   */
  public static void setProcessorForRequest(Request request, RequestProcessor processor) {
    processors.put(getRequestHashCode(request), processor);
  }
}
