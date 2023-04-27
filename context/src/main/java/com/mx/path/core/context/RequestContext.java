package com.mx.path.core.context;

import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.mx.path.core.common.collection.SingleValueMap;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class RequestContext {

  private static final ThreadLocal<RequestContext> THREAD_LOCAL = new ThreadLocal<>();

  /**
   * Clear ThreadLocal from current RequestContext
   */
  public static void clear() {
    THREAD_LOCAL.remove();
  }

  /**
   * Current active RequestContext on ThreadLocal
   *
   * @return RequestContext
   */
  public static RequestContext current() {
    return THREAD_LOCAL.get();
  }

  private String clientGuid;
  private String clientId;
  private String feature;
  @Setter
  private String originatingIP;
  private String path;
  private String sessionTraceId;
  private String deviceTraceId;
  private String userGuid;
  @Deprecated
  private String userId;
  private SingleValueMap<String, Object> headers;
  private SingleValueMap<String, Object> params;

  public abstract static class RequestContextBuilder<C extends RequestContext, B extends RequestContext.RequestContextBuilder<C, B>> {

    private SingleValueMap<String, Object> headers = new SingleValueMap<>();
    private SingleValueMap<String, Object> params = new SingleValueMap<>();

    public final B parameter(String k, String v) {
      params.put(k, v);

      return self();
    }

    public final B withParameters(Consumer<SingleValueMap<String, Object>> consumer) {
      consumer.accept(params);

      return self();
    }

    public final B header(String k, String v) {
      headers.put(k, v);

      return self();
    }

    public final B withHeaders(Consumer<SingleValueMap<String, Object>> consumer) {
      consumer.accept(headers);

      return self();
    }
  }

  /**
   * Sets this RequestContext on as the current().
   * Note: This uses {@link ThreadLocal}.
   */
  public void register() {
    THREAD_LOCAL.set(this);
  }

  /**
   * Create a context if one is not already present.
   * @param clientId
   * @param function
   */
  public static void withSelfClearing(String clientId, Consumer<RequestContext> function) {
    boolean selfClearContext = false;

    if (RequestContext.current() == null) {
      RequestContext.builder().clientId(clientId).build().register();
      selfClearContext = true;
    }

    try {
      function.accept(RequestContext.current());
    } finally {
      if (selfClearContext) {
        RequestContext.clear();
      }
    }
  }

}
