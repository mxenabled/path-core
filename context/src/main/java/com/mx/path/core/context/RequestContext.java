package com.mx.path.core.context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.mx.path.core.common.collection.SingleValueMap;

/**
 * Context class for request.
 */
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class RequestContext {

  private static final ThreadLocal<RequestContext> THREAD_LOCAL = new ThreadLocal<>();

  /**
   * Clear {@link ThreadLocal} from current {@link RequestContext}.
   */
  public static void clear() {
    THREAD_LOCAL.remove();
  }

  /**
   * Current active {@link RequestContext} on {@link ThreadLocal}.
   *
   * @return {@link RequestContext}
   */
  public static RequestContext current() {
    return THREAD_LOCAL.get();
  }

  /**
   * Request context guid.
   *
   * -- GETTER --
   * Return context guid.
   *
   * @return context guid
   *
   * -- SETTER --
   * Set context guid.
   *
   * @param clientGuid context guid
   */
  private String clientGuid;

  /**
   * Request client id.
   *
   * -- GETTER --
   * Return client id.
   *
   * @return client id
   *
   * -- SETTER --
   * Set client id.
   *
   * @param clientId client id
   */
  private String clientId;

  /**
   * Request feature.
   *
   * -- GETTER --
   * Return feature.
   *
   * @return feature
   *
   * -- SETTER --
   * Set feature.
   *
   * @param feature feature
   */
  private String feature;

  /**
   * Request originating ip.
   *
   * -- GETTER --
   * Return originating ip.
   *
   * @return originating ip
   *
   * -- SETTER --
   * Set originating ip.
   *
   * @param originatingIP originating ip
   */
  @Setter
  private String originatingIP;

  /**
   * Getter returning originating ip address
   */
  @SuppressWarnings("PMD.EmptyCatchBlock")
  public String getOriginatingIP() {
    if (originatingIP == null) {
      try {
        URL url = new URL("https://api.ipify.org"); // Public IP service URL
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
        originatingIP = in.readLine();
        in.close();
      } catch (IOException e) {
        //Do nothing as we don't want to obstruct the flow
      }
    }
    return originatingIP;
  }

  /**
   * Request path.
   *
   * -- GETTER --
   * Return path.
   *
   * @return path
   *
   * -- SETTER --
   * Set path.
   *
   * @param path path
   */
  private String path;

  /**
   * Request session trace id.
   *
   * -- GETTER --
   * Return session trace id.
   *
   * @return session trace id
   *
   * -- SETTER --
   * Set session trace id.
   *
   * @param sessionTraceId session trace id
   */
  private String sessionTraceId;

  /**
   * Request device trace id.
   *
   * -- GETTER --
   * Return device trace id.
   *
   * @return device trace id
   *
   * -- SETTER --
   * Set device trace id.
   *
   * @param deviceTraceId device trace id
   */
  private String deviceTraceId;

  /**
   * Request user guid.
   *
   * -- GETTER --
   * Return user guid.
   *
   * @return user guid
   *
   * -- SETTER --
   * Set user guid.
   *
   * @param userGuid user guid
   */
  private String userGuid;

  /**
   * Request user id.
   *
   * -- GETTER --
   * Return user id.
   *
   * @return user id
   *
   * -- SETTER --
   * Set user id.
   *
   * @param userId user id
   */
  @Deprecated
  private String userId;

  /**
   * Headers for current request.
   */
  private SingleValueMap<String, Object> headers;

  /**
   * Function parameters for current request.
   */
  private SingleValueMap<String, Object> params;

  @Builder.Default
  private UpstreamRequestConfiguration upstreamRequestConfiguration = new UpstreamRequestConfiguration();

  /**
   * Helper builder class for request context.
   *
   * @param <C> request context type
   * @param <B> builder type
   */
  public abstract static class RequestContextBuilder<C extends RequestContext, B extends RequestContext.RequestContextBuilder<C, B>> {

    private SingleValueMap<String, Object> headers = new SingleValueMap<>();
    private SingleValueMap<String, Object> params = new SingleValueMap<>();

    /**
     * Add new parameter with given key and value.
     *
     * @param k key
     * @param v value
     * @return self
     */
    public final B parameter(String k, String v) {
      params.put(k, v);

      return self();
    }

    /**
     * Set parameters on given consumer.
     *
     * @param consumer consumer
     * @return self
     */
    public final B withParameters(Consumer<SingleValueMap<String, Object>> consumer) {
      consumer.accept(params);

      return self();
    }

    /**
     * Add new header with given key and value.
     *
     * @param k key
     * @param v value
     * @return self
     */
    public final B header(String k, String v) {
      headers.put(k, v);

      return self();
    }

    /**
     * Add header to given consumer.
     *
     * @param consumer consumer
     * @return self
     */
    public final B withHeaders(Consumer<SingleValueMap<String, Object>> consumer) {
      consumer.accept(headers);

      return self();
    }
  }

  /**
   * Sets this {@link RequestContext} on as the {@link #current()}.
   *
   * Note: This uses {@link ThreadLocal}.
   */
  public void register() {
    THREAD_LOCAL.set(this);
  }

  /**
   * Create a context if one is not already present.
   *
   * @param clientId client id
   * @param function function
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
