package com.mx.path.core.common.connect;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.github.rholder.retry.Retryer;
import com.mx.path.core.common.collection.MultiValueMap;
import com.mx.path.core.common.collection.MultiValueMappable;
import com.mx.path.core.common.collection.SingleValueMap;
import com.mx.path.core.common.process.RetriesFailedException;
import com.mx.path.core.common.request.Feature;

/**
 * Base class for requests.
 *
 * <p>Implements a fluent programming interface. We need the type of the extender in {@param REQ}.
 * This allows us to maintain the true type in the call chain without reverting to {@link Request}.
 *
 * <p>Example:
 *
 * <pre>{@code
 *   public class SocketRequest extends Request<SocketRequest, SocketResponse> {
 *     public SocketResponse send() {
 *       // ...
 *     }
 *   }
 * }</pre>
 *
 * <p>Usage:
 *
 * <pre>{@code
 * new SocketRequest()
 *   .withBaseUrl("127.0.0.1")
 *   .withBody("ACK")
 *   .send(); // still have SocketRequest type!
 * }</pre>
 *
 * @param <REQ> The type of the request. (self-reference)
 * @param <RESP> Type of the associated response
 */
@EqualsAndHashCode
public abstract class Request<REQ extends Request<?, ?>, RESP extends Response<?, ?>> {

  public enum PreferredResponseBodyType {
    /**
     * Uses the Content-Type headers to infer what body-type should be returned.
     */
    INFERRED_FROM_CONTENT_TYPE,
    /**
     * Converts the response body to a string before returning the response.
     */
    STRING,
    /**
     * Adds the raw response body before returning the response.
     */
    RAW,
    /**
     * Converts the response body to a String and adds the raw response body before returning the response.
     */
    STRING_AND_RAW
  }

  private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofMillis(10000);
  private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofMillis(30000);

  // Fields

  /**
   * -- GETTER --
   * Return number of attempts.
   *
   * @return number of attempts
   */
  @Getter
  private int attemptCount = 0;

  /**
   * -- GETTER --
   * Return base url.
   *
   * @return base url
   * -- SETTER --
   * Set base url.
   *
   * @param baseUrl base url to set
   */
  @Getter
  @Setter
  private String baseUrl = null;

  /**
   * -- GETTER --
   * Return body.
   *
   * @return body
   * -- SETTER --
   * Set body.
   *
   * @param body body to set
   */
  @Getter
  @Setter
  private Object body;

  /**
   * -- GETTER --
   * Return json body.
   *
   * @return json body
   * -- SETTER --
   * Set json body.
   *
   * @param bodyJson json body to set
   */
  @Getter
  @Setter
  private String bodyJson;

  /**
   * -- GETTER --
   * Return connection settings.
   *
   * @return connection settings
   * -- SETTER --
   * Set connection settings.
   *
   * @param connectionSettings connection settings to set
   */
  @Getter
  @Setter
  private ConnectionSettings connectionSettings;

  /**
   * -- SETTER --
   * Set limit for connect timeout.
   *
   * @param connectTimeout limit for connect timeout to set
   */
  @Setter
  private Duration connectTimeout;

  /**
   * -- GETTER --
   * Return fault tolerant scope.
   *
   * @return fault tolerant scope
   * -- SETTER --
   * Set fault tolerant scope.
   *
   * @param faultTolerantScope fault tolerant scope to set
   */
  @Getter
  @Setter
  private String faultTolerantScope;

  /**
   * -- GETTER --
   * Return feature.
   *
   * @return feature
   * -- SETTER --
   * Set feature.
   *
   * @param feature feature to set
   */
  @Getter
  @Setter
  private Feature feature;

  /**
   * -- GETTER --
   * Return filter chain.
   *
   * @return filter chain
   * -- SETTER --
   * Set filter chain.
   *
   * @param filterChain filter chain to set
   */
  @Getter
  @Setter
  private RequestFilter filterChain;

  /**
   * -- GETTER --
   * Return form body.
   *
   * @return form body
   * -- SETTER --
   * Set form body.
   *
   * @param formBody form body to set
   */
  @Getter
  @Setter
  private FormBody formBody;

  /**
   * -- GETTER --
   * Return header.
   *
   * @return header
   */
  @Getter
  private SingleValueMap<String, String> headers = new SingleValueMap<>();

  /**
   * -- GETTER --
   * Return method.
   *
   * @return method
   * -- SETTER --
   * Set method.
   *
   * @param method method to set
   */
  @Getter
  @Setter
  private String method = "GET";

  /**
   * -- GETTER --
   * Return on complete.
   *
   * @return on complete
   * -- SETTER --
   * Set on complete.
   *
   * @param onComplete on complete to set
   */
  @Getter
  @Setter
  private Consumer<RESP> onComplete;

  /**
   * -- GETTER --
   * Return path.
   *
   * @return path
   * -- SETTER --
   * Set path.
   *
   * @param path path to set
   */
  @Getter
  @Setter
  private String path = "";

  /**
   * -- GETTER --
   * Return preferred response body type.
   *
   * @return preferred response body type
   * -- SETTER --
   * Set preferred response body type.
   *
   * @param preferredResponseBodyType preferred response body type to set
   */
  @Getter
  @Setter
  private PreferredResponseBodyType preferredResponseBodyType = PreferredResponseBodyType.INFERRED_FROM_CONTENT_TYPE;

  /**
   * -- GETTER --
   * Return processor.
   *
   * @return processor
   * -- SETTER --
   * Set processor.
   *
   * @param processor processor to set
   */
  @Getter
  @Setter
  private Function<RESP, Object> processor;

  /**
   * -- GETTER --
   * Return query string parameters.
   *
   * @return query string parameters
   * -- SETTER --
   * Set query string parameters.
   *
   * @param queryStringParams query string parameters to set
   */
  @Getter
  @Setter
  private SingleValueMap<String, String> queryStringParams = new SingleValueMap<>();

  /**
   * -- GETTER --
   * Return retry configuration.
   *
   * @return retry configuration
   * -- SETTER --
   * Set retry configuration.
   *
   * @param responseRetryConfiguration retry configuration to set
   */
  @Getter
  @Setter
  private ResponseRetryConfiguration<RESP> responseRetryConfiguration = null;

  /**
   * -- GETTER --
   * Return query retry exception supplier.
   *
   * @return query retry exception supplier
   * -- SETTER --
   * Set query retry exception supplier.
   *
   * @param responseRetryExceptionSupplier query retry exception supplier to set
   */
  @Getter
  @Setter
  private Function<Throwable, RuntimeException> responseRetryExceptionSupplier = null;

  /**
   * -- GETTER --
   * Return start nano.
   *
   * @return start nano
   */
  @Getter
  private long startNano = 0;

  /**
   * -- SETTER --
   * Set timeout limit.
   *
   * @param timeOut timeout limit to set
   */
  @Setter
  @Deprecated
  private Duration timeOut;

  /**
   * -- SETTER --
   * Set request timeout limit.
   *
   * @param requestTimeout request timeout limit to set
   */
  @Setter
  private Duration requestTimeout;

  /**
   * -- GETTER --
   * Return trace id.
   *
   * @return trace id
   * -- SETTER --
   * Set trace id.
   *
   * @param traceId trace id to set
   */
  @Getter
  @Setter
  private String traceId;

  /**
   * -- GETTER --
   * Return trace span id.
   *
   * @return trace span id
   * -- SETTER --
   * Set trace span id.
   *
   * @param traceSpanId trace span id to set
   */
  @Getter
  @Setter
  private String traceSpanId;

  // Constructors

  /**
   * Build new {@link Request} instance with specified filter chain.
   *
   * @param filterChain filter chain
   */
  @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
  public Request(RequestFilter filterChain) {
    headers.put("Accept", "application/json");
    headers.put("Content-Type", "application/json");
    if (filterChain == null) {
      throw new MisConfiguredFilterChainException();
    }
    setFilterChain(filterChain);
  }

  // Public Methods

  /**
   * Called when request is complete. Executes #getOnComplete().
   *
   * @param currentResponse current response
   * @throws RuntimeException to thrown
   */
  public void completed(RESP currentResponse) throws RuntimeException {
    if (getOnComplete() != null) {
      getOnComplete().accept(currentResponse);
    }
  }

  /**
   * Execute this request.
   *
   * @return Response
   */
  public RESP execute() {
    if (responseRetryConfiguration != null) {
      return executeWithRetryConfiguration();
    }

    RESP response = newResponse();
    getFilterChain().execute(this, response);

    return response;
  }

  /**
   * Get accept header.
   *
   * @return header
   */
  public final String getAccept() {
    return headers.get("Accept");
  }

  /**
   * @return Connect timeout in milliseconds
   */
  public final Duration getConnectTimeout() {
    connectTimeout = (connectTimeout == null) ? connectionSettings.getConnectTimeout() : connectTimeout;
    connectTimeout = (connectTimeout == null) ? DEFAULT_CONNECT_TIMEOUT : connectTimeout;
    return connectTimeout;
  }

  /**
   * @return content type header
   */
  public final String getContentType() {
    return headers.get("Content-Type");
  }

  /**
   * @return headers as multi map
   */
  public final MultiValueMap<String, String> getHeadersAsMultiValueMap() {
    return new MultiValueMap<>(headers);
  }

  /**
   * @return headers as single value map
   */
  public final SingleValueMap<String, String> getHeadersAsSingleValueMap() {
    return new SingleValueMap<>(headers);
  }

  /**
   * @return Request timeout in milliseconds
   */
  @Deprecated
  public final Duration getRequestTimeOut() {
    return requestTimeout == null ? DEFAULT_REQUEST_TIMEOUT : requestTimeout;
  }

  /**
   * @return Request timeout in milliseconds
   */
  public final Duration getRequestTimeout() {
    requestTimeout = (requestTimeout == null) ? connectionSettings.getRequestTimeout() : requestTimeout;
    requestTimeout = (requestTimeout == null) ? DEFAULT_REQUEST_TIMEOUT : requestTimeout;
    return requestTimeout;
  }

  /**
   * @return trace key
   */
  public final String getTraceKey() {
    return getMethod() + ":" + getPath();
  }

  /**
   * @return uri
   */
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

  /**
   * @return true if body is not null
   */
  public final boolean hasBody() {
    return body != null;
  }

  /**
   * Override to build new instance of concrete request type.
   *
   * @return instance of {@link RESP}
   */
  public abstract RESP newResponse();

  public final Object process(RESP currentResponse) {
    if (this.processor != null) {
      return this.processor.apply(currentResponse);
    }

    return null;
  }

  /**
   * Append new header.
   *
   * @param key header key
   * @param value header value
   */
  public final void setHeader(String key, String value) {
    getHeaders().put(key, value);
  }

  /**
   * Set headers with single value map.
   *
   * @param singleValueMap map to set
   */
  public final void setHeaders(MultiValueMappable<String, String> singleValueMap) {
    this.headers = new SingleValueMap<>(singleValueMap);
  }

  /**
   * Set request timeout.
   *
   * @param timeout timeout
   */
  @Deprecated
  public final void setTimeout(Duration timeout) {
    this.requestTimeout = timeout;
  }

  /**
   * Called before the request starts. Sets start time. Override to add behavior. Be sure to call {@code super.start()}
   */
  public void start() {
    if (attemptCount < 1) {
      attemptCount = 1;
    }
    if (startNano == 0) {
      startNano = System.nanoTime();
    }
  }

  /**
   * Called before retrying request. Sets start time. Override to add behavior. Be sure to call {@code super.startRetry()}
   */
  public void startRetry() {
    attemptCount++;
    startNano = 0;
  }

  /**
   * Append or update accept header.
   *
   * @param accept header value
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withAccept(String accept) {
    if (accept == null) {
      headers.remove("Accept");
    } else {
      headers.put("Accept", accept);
    }

    return (REQ) this;
  }

  /**
   * Set base url.
   *
   * @param newBaseUrl base url to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withBaseUrl(String newBaseUrl) {
    setBaseUrl(newBaseUrl);
    return (REQ) this;
  }

  /**
   * Set body.
   *
   * @param newBody body to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withBody(Object newBody) {
    setBody(newBody);
    return (REQ) this;
  }

  /**
   * Set body json.
   *
   * @param newBodyJson body json to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withBodyJson(String newBodyJson) {
    setBodyJson(newBodyJson);
    return (REQ) this;
  }

  /**
   * Set connection settings.
   *
   * @param newConnectionSettings connection settings to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withConnectionSettings(ConnectionSettings newConnectionSettings) {
    setConnectionSettings(newConnectionSettings);
    return (REQ) this;
  }

  /**
   * Connect timeout as Duration.
   *
   * @param connectionTimeout  timeout to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withConnectTimeout(Duration connectionTimeout) {
    setConnectTimeout(connectionTimeout);
    return (REQ) this;
  }

  /**
   * Set content type header.
   *
   * @param contentType header to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withContentType(String contentType) {
    if (contentType == null) {
      headers.remove("Content-Type");
    } else {
      headers.put("Content-Type", contentType);
    }

    return (REQ) this;
  }

  /**
   * Set fault tolerance scope.
   *
   * @param newFaultTolerantScope fault tolerance scope to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withFaultTolerantScope(String newFaultTolerantScope) {
    setFaultTolerantScope(newFaultTolerantScope);
    return (REQ) this;
  }

  /**
   * Set feature.
   *
   * @param newFeature feature to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withFeature(Feature newFeature) {
    setFeature(newFeature);
    return (REQ) this;
  }

  /**
   * Set form body.
   *
   * @param newFormBody form body to set.
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withFormBody(FormBody newFormBody) {
    setFormBody(newFormBody);
    return (REQ) this;
  }

  /**
   * Set new header.
   *
   * @param key header key
   * @param value header value
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withHeader(String key, String value) {
    setHeader(key, value);
    return (REQ) this;
  }

  /**
   * Append new consumer headers.
   *
   * @param headerConsumer headers to append
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withHeaders(Consumer<Map<String, String>> headerConsumer) {
    headerConsumer.accept(this.headers);
    return (REQ) this;
  }

  /**
   * Set method.
   *
   * @param newMethod method to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withMethod(String newMethod) {
    setMethod(newMethod);
    return (REQ) this;
  }

  /**
   * Set on complete.
   * @param newOnComplete on complete to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withOnComplete(Consumer<RESP> newOnComplete) {
    setOnComplete(newOnComplete);
    return (REQ) this;
  }

  /**
   * Set path.
   *
   * @param newPath path to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withPath(String newPath) {
    setPath(newPath);
    return (REQ) this;
  }

  /**
   * Set preferred response body type.
   *
   * @param responseBodyTypePreference preferred response body type to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withPreferredResponseBodyType(PreferredResponseBodyType responseBodyTypePreference) {
    setPreferredResponseBodyType(responseBodyTypePreference);
    return (REQ) this;
  }

  /**
   * Set processor.
   *
   * @param newOnProcess processor to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withProcessor(Function<RESP, Object> newOnProcess) {
    setProcessor(newOnProcess);
    return (REQ) this;
  }

  /**
   * Set new query string parameter.
   * @param key parameter key
   * @param value parameter value
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withQueryStringParam(String key, String value) {
    this.queryStringParams.put(key, value);
    return (REQ) this;
  }

  /**
   * Set new query string parameters.
   *
   * @param newQueryStringParams parameters to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withQueryStringParams(SingleValueMap<String, String> newQueryStringParams) {
    setQueryStringParams(newQueryStringParams);
    return (REQ) this;
  }

  /**
   * Set new query string parameters.
   *
   * @param newQueryStringParams parameters to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withQueryStringParams(Consumer<SingleValueMap<String, String>> newQueryStringParams) {
    newQueryStringParams.accept(getQueryStringParams());
    return (REQ) this;
  }

  /**
   * Use {@link Retryer} to execute this request.
   *
   * <p>To streamline the configuration of retryers prefer {@link #withResponseRetryConfiguration(ResponseRetryConfiguration)}. Only use this
   * if you need more advanced control over the construction of the {@link Retryer} (rare).
   *
   * <p>The reties will attempt to execute the request from the top of the request filter stack. Each attempt will get
   * a new {@link Response}. Be sure to set a retryIfResult predicate, otherwise retries will never occur.
   *
   * <p><strong>Considerations</strong>
   * <p>Given that fault-tolerant executor executes within each retry, care must be taken when retrying long-running.
   * This will likely result in global timeouts or non-responsive request executes. Ideally, retries can be used when
   * the upstream system responds quickly with a particular status if the system is too busy, indicating that a retry
   * is likely to succeed.
   *
   * <p>When a retryer is set, if all attempts fail, a {@link UpstreamSystemUnavailableAfterRetries} exception
   * will be set on the response. It's cause will be the exception from the last attempt, if there was one.
   *
   * <p><strong>Examples:</strong>
   *
   * <p><strong>Simple retry</strong>
   *
   * <p>This will attempt the call 3 times with no delay. A retry will occur when the HttpStatus is ACCEPTED
   *
   * <pre>{@code
   *   .withRetryer(RetryerBuilder.<TestResponse>newBuilder()
   *                              .retryIfResult((response) -> response.getStatus() == HttpStatus.ACCEPTED) // always retry
   *                              .withStopStrategy(StopStrategies.stopAfterAttempt(3))
   *                              .build())
   * }</pre>
   *
   * <p><strong>Backoff Retry</strong>
   *
   * <p>This will attempt the call with an increasing delay between attempts. It will stop attempts 15 seconds
   * after the first attempt. A retry will occur when the HttpStatus is BANDWIDTH_LIMIT_EXCEEDED
   *
   * <pre>{@code
   *   .withRetryer(RetryerBuilder.<TestResponse>newBuilder()
   *                              .retryIfResult((response) -> response.getStatus() == HttpStatus.BANDWIDTH_LIMIT_EXCEEDED) // always retry
   *                              .withStopStrategy(StopStrategies.stopAfterDelay(15, TimeUnit.SECONDS))
   *                              .withWaitStrategy(WaitStrategies.incrementingWait(500, TimeUnit.MILLISECONDS, 500, TimeUnit.MILLISECONDS))
   *                              .build())
   * }</pre>
   *
   * @param newRetryer Retryer to use when executing this request. (default: null)
   * @return this
   */
  @SuppressWarnings("unchecked")
  public final REQ withRetryer(Retryer<RESP> newRetryer) {
    setResponseRetryConfiguration(new ResponseRetryConfiguration(newRetryer));
    return (REQ) this;
  }

  /**
   * The preferred way to provide a retryer.
   *
   * <p>{@link ResponseRetryConfiguration} can be added to bound configuration POJO and passed directly into this for relevant
   * configurations.
   * @param newResponseRetryConfiguration retry configuration to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withResponseRetryConfiguration(ResponseRetryConfiguration newResponseRetryConfiguration) {
    setResponseRetryConfiguration(newResponseRetryConfiguration);
    return (REQ) this;
  }

  /**
   * The preferred way to provide a retryer.
   *
   * <p>{@link ResponseRetryConfiguration} can be added to bound configuration POJO and passed directly into this for relevant
   * configurations.
   * @param newResponseRetryConfiguration retry configuration to set
   * @param exceptionSupplierOverride supplier to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withResponseRetryConfiguration(ResponseRetryConfiguration newResponseRetryConfiguration, Function<Throwable, RuntimeException> exceptionSupplierOverride) {
    setResponseRetryConfiguration(newResponseRetryConfiguration);
    setResponseRetryExceptionSupplier(exceptionSupplierOverride);
    return (REQ) this;
  }

  /**
   * Request timeout as Duration.
   *
   * @return self
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public final REQ withTimeOut(Duration requestTimeOut) {
    setRequestTimeout(requestTimeOut);
    return (REQ) this;
  }

  /**
   * Request timeout as Duration.
   *
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final REQ withRequestTimeout(Duration timeout) {
    setRequestTimeout(timeout);
    return (REQ) this;
  }

  /**
   * Execute this request using configured retryer.
   *
   * @return response
   */
  protected RESP executeWithRetryConfiguration() {
    AtomicReference<RESP> response = new AtomicReference<>();
    try {
      if (responseRetryExceptionSupplier != null) { // Call with exceptionSupplier override
        return responseRetryConfiguration.call(() -> {
          if (attemptCount > 0) { // This is a retry. Setup for next attempt
            startRetry();
          }
          response.set(newResponse());
          getFilterChain().execute(this, response.get());
          return response.get();
        }, responseRetryExceptionSupplier);
      } else {
        return responseRetryConfiguration.call(() -> {
          if (attemptCount > 0) { // This is a retry. Setup for next attempt
            startRetry();
          }
          response.set(newResponse());
          getFilterChain().execute(this, response.get());
          return response.get();
        });
      }
    } catch (RetriesFailedException e) {
      response.get().setException(new UpstreamSystemUnavailableAfterRetries("Upstream call failed after retries", e));
    }

    return response.get();
  }

}
