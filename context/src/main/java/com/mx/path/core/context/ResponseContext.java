package com.mx.path.core.context;

import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import com.mx.path.core.common.collection.SingleValueMap;

/**
 * Context class for response.
 */
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class ResponseContext {
  private static final ThreadLocal<ResponseContext> THREAD_LOCAL = new ThreadLocal<>();

  /**
   * Clear {@link ThreadLocal} from current {@link ResponseContext}.
   */
  public static void clear() {
    THREAD_LOCAL.remove();
  }

  /**
   * Current active {@link ResponseContext} on {@link ThreadLocal}.
   *
   * @return {@link ResponseContext}
   */
  public static ResponseContext current() {
    return THREAD_LOCAL.get();
  }

  /**
   * Create a context if one is not already present.
   *
   * @param consumer block to run with context
   */
  public static void withSelfClearing(Consumer<ResponseContext> consumer) {
    boolean selfClearContext = false;

    if (ResponseContext.current() == null) {
      ResponseContext.builder().build().register();
      selfClearContext = true;
    }

    try {
      consumer.accept(ResponseContext.current());
    } finally {
      if (selfClearContext) {
        ResponseContext.clear();
      }
    }
  }

  /**
   * Helper class to build response context.
   *
   * @param <C> type of response being built
   * @param <B> type of builder
   */
  public abstract static class ResponseContextBuilder<C extends ResponseContext, B extends ResponseContext.ResponseContextBuilder<C, B>> {
    private SingleValueMap<String, String> headers = new SingleValueMap<>();

    /**
     * Add a header.
     *
     * @param key header key
     * @param value header value
     * @return this builder
     */
    public B header(String key, String value) {
      headers.put(key, value);

      return self();
    }
  }

  private SingleValueMap<String, String> headers;

  /**
   * Default constructor.
   */
  public ResponseContext() {
    headers = new SingleValueMap<>();
  }

  /**
   * Registers this instance as {@link ResponseContext#current()}.
   */
  public void register() {
    THREAD_LOCAL.set(this);
  }
}
