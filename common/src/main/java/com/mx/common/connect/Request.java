package com.mx.common.connect;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.mx.common.collections.MultiValueMap;
import com.mx.common.collections.MultiValueMappable;
import com.mx.common.collections.SingleValueMap;
import com.mx.common.exception.request.accessor.AccessorSystemException;
import com.mx.common.request.Feature;

/**
 * Base class for requests.
 *
 * Implements a fluent programming interface. We need the type of the extender in {@param REQ}.
 * This allows us to maintain the true type in the call chain without reverting to {@link Request}.
 * <p>
 * Example:
 *
 * <pre>
 * {@code
 *   public class SocketRequest extends Request<SocketRequest, SocketResponse> {
 *     public SocketResponse send() {
 *       // ...
 *     }
 *   }
 * }
 * </pre>
 * </p>
 * <p>
 * Usage:
 * <pre>
 * {@code
 * new SocketRequest()
 *   .withBaseUrl("127.0.0.1")
 *   .withBody("ACK")
 *   .send(); // still have SocketRequest type!
 * }
 * </pre>
 * </p>
 * @param <REQ> The type of the request. (self-reference)
 * @param <RESP> Type of the associated response
 */
@EqualsAndHashCode
public abstract class Request<REQ extends Request<?, ?>, RESP extends Response<?, ?>> {

  public enum PreferredResponseBodyType {
    // Uses the Content-Type headers to infer what body-type should be returned.
    INFERRED_FROM_CONTENT_TYPE,
    // Converts the response body to a string before returning the response
    STRING,
    // Adds the raw response body before returning the response
    RAW,
    // Converts the response body to a String and adds the raw response body before returning the response.
    STRING_AND_RAW
  }

  private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofMillis(30000);

  // Fields

  @Getter
  @Setter
  private String baseUrl = null;

  @Getter
  @Setter
  private Object body;

  @Getter
  @Setter
  private String bodyJson;

  @Getter
  @Setter
  private ConnectionSettings connectionSettings;

  @Getter
  @Setter
  private String faultTolerantScope;

  @Getter
  @Setter
  private Feature feature;

  @Deprecated
  private String featureName = null;

  @Getter
  @Setter
  private RequestFilter filterChain;

  @Getter
  @Setter
  private FormBody formBody;

  @Getter
  private SingleValueMap<String, String> headers = new SingleValueMap<>();

  @Deprecated
  private boolean isCircuitBreakerOpen = false;

  @Getter
  @Setter
  private String method = "GET";

  @Getter
  @Setter
  private Consumer<RESP> onComplete;

  @Getter
  @Setter
  private String path = "";

  @Getter
  @Setter
  private PreferredResponseBodyType preferredResponseBodyType = PreferredResponseBodyType.INFERRED_FROM_CONTENT_TYPE;

  @Getter
  @Setter
  private Function<RESP, Object> processor;

  @Getter
  @Setter
  private SingleValueMap<String, String> queryStringParams = new SingleValueMap<>();

  @Getter
  private long startNano = 0;

  @Setter
  private Duration timeOut;

  @Getter
  @Setter
  private String traceId;

  @Getter
  @Setter
  private String traceSpanId;

  // Getters/Setters

  @Deprecated
  public final String getFeatureName() {
    return featureName;
  }

  // Constructors

  /**
   * @deprecated Left here for backward compatibility. Will be removed. Use {@link Request(RequestFilter)}
   */
  @Deprecated
  public Request() {
    headers.put("Accept", "application/json");
    headers.put("Content-Type", "application/json");
  }

  public Request(RequestFilter filterChain) {
    headers.put("Accept", "application/json");
    headers.put("Content-Type", "application/json");
    if (filterChain == null) {
      throw new AccessorSystemException("Mis-configured Request. filterChain is null");
    }
    setFilterChain(filterChain);
  }

  // Public Methods

  /**
   * Called when request is complete. Executes {@link #getOnComplete()}
   * @param currentResponse
   * @throws RuntimeException
   */
  public void completed(RESP currentResponse) throws RuntimeException {
    if (getOnComplete() != null) {
      getOnComplete().accept(currentResponse);
    }
  }

  /**
   * Execute this request
   * @return Response
   */
  public abstract RESP execute();

  public final String getAccept() {
    return headers.get("Accept");
  }

  public final String getContentType() {
    return headers.get("Content-Type");
  }

  public final MultiValueMap<String, String> getHeadersAsMultiValueMap() {
    return new MultiValueMap<>(headers);
  }

  public final SingleValueMap<String, String> getHeadersAsSingleValueMap() {
    return new SingleValueMap<>(headers);
  }

  /**
   * @deprecated Use {@link #getConnectionSettings()}
   */
  @Deprecated
  public final ConnectionSettings getMutualAuthSettings() {
    return this.connectionSettings;
  }

  /**
   * @return Request timeout in milliseconds
   */
  public final Duration getRequestTimeOut() {
    return timeOut == null ? DEFAULT_REQUEST_TIMEOUT : timeOut;
  }

  public final String getTraceKey() {
    return getMethod() + ":" + getPath();
  }

  public final String getUri() {
    StringBuilder uri = new StringBuilder(baseUrl);
    StringBuilder p = new StringBuilder(path);

    while (uri.length() > 1 && uri.charAt(uri.length() - 1) == '/') {
      uri.setLength(uri.length() - 1);
    }
    while (p.length() > 1 && p.charAt(0) == '/') {
      p.deleteCharAt(0);
    }

    if (p.length() > 1) {
      uri.append('/');
      uri.append(p);
    }

    return uri.toString();
  }

  public final boolean hasBody() {
    return body != null;
  }

  /**
   * @deprecated This should be in the response. Leaving for backward compatibility.
   */
  @Deprecated
  public final boolean isCircuitBreakerOpen() {
    return isCircuitBreakerOpen;
  }

  public final Object process(RESP currentResponse) {
    if (this.processor != null) {
      return this.processor.apply(currentResponse);
    }

    return null;
  }

  /**
   * @deprecated This should be in the response. Leaving for backward compatibility.
   */
  @Deprecated
  public final void setCircuitBreakerOpen(boolean circuitBreakerOpen) {
    isCircuitBreakerOpen = circuitBreakerOpen;
  }

  public final void setHeader(String key, String value) {
    getHeaders().put(key, value);
  }

  public final void setHeaders(MultiValueMappable<String, String> singleValueMap) {
    this.headers = new SingleValueMap<>(singleValueMap);
  }

  /**
   * Called before the request starts. Sets start time. Override to add behavior. Be sure to call {@code super.start()}
   */
  public void start() {
    if (startNano == 0) {
      startNano = System.nanoTime();
    }
  }

  @SuppressWarnings("unchecked")
  public final REQ withAccept(String accept) {
    if (accept == null) {
      headers.remove("Accept");
    } else {
      headers.put("Accept", accept);
    }

    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withBaseUrl(String newBaseUrl) {
    setBaseUrl(newBaseUrl);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withBody(Object newBody) {
    setBody(newBody);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withBodyJson(String newBodyJson) {
    setBodyJson(newBodyJson);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withConnectionSettings(ConnectionSettings newConnectionSettings) {
    setConnectionSettings(newConnectionSettings);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withContentType(String contentType) {
    if (contentType == null) {
      headers.remove("Content-Type");
    } else {
      headers.put("Content-Type", contentType);
    }

    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withFaultTolerantScope(String newFaultTolerantScope) {
    setFaultTolerantScope(newFaultTolerantScope);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withFeature(Feature newFeature) {
    setFeature(newFeature);
    return (REQ) this;
  }

  @Deprecated
  @SuppressWarnings("unchecked")
  public final REQ withFeatureName(String featureKeyName) {
    featureName = featureKeyName;
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withFormBody(FormBody newFormBody) {
    setFormBody(newFormBody);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withHeader(String key, String value) {
    setHeader(key, value);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withHeaders(Consumer<Map<String, String>> headerConsumer) {
    headerConsumer.accept(this.headers);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withMethod(String newMethod) {
    setMethod(newMethod);
    return (REQ) this;
  }

  /**
   * @deprecated Use {@link #withConnectionSettings(ConnectionSettings)}.
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public final REQ withMutualAuthSettings(ConnectionSettings newMutualAuthSettings) {
    return withConnectionSettings(newMutualAuthSettings);
  }

  @SuppressWarnings("unchecked")
  public final REQ withOnComplete(Consumer<RESP> newOnComplete) {
    setOnComplete(newOnComplete);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withPath(String newPath) {
    setPath(newPath);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withPreferredResponseBodyType(PreferredResponseBodyType responseBodyTypePreference) {
    setPreferredResponseBodyType(responseBodyTypePreference);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withProcessor(Function<RESP, Object> newOnProcess) {
    setProcessor(newOnProcess);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withQueryStringParam(String key, String value) {
    this.queryStringParams.put(key, value);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withQueryStringParams(SingleValueMap<String, String> newQueryStringParams) {
    setQueryStringParams(newQueryStringParams);
    return (REQ) this;
  }

  @SuppressWarnings("unchecked")
  public final REQ withQueryStringParams(Consumer<SingleValueMap<String, String>> newQueryStringParams) {
    newQueryStringParams.accept(getQueryStringParams());
    return (REQ) this;
  }

  /**
   * Request timeout as Duration
   * @return this
   */
  @SuppressWarnings("unchecked")
  public final REQ withTimeOut(Duration requestTimeOut) {
    setTimeOut(requestTimeOut);
    return (REQ) this;
  }
}
