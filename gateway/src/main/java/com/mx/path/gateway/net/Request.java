package com.mx.path.gateway.net;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mx.accessors.ConnectionSettings;
import com.mx.common.collections.SingleValueMap;
import com.mx.common.request.Feature;

import io.opentracing.SpanContext;

public class Request {
  public enum PreferredResponseBodyType {
    // Uses the Content-Type headers to infer what body-type should be returned.
    INFERRED_FROM_CONTENT_TYPE,
    // Converts the response body to a string before returning the response
    STRING,
    // Adds the raw response body before returning the response
    RAW,
    // Converts the response body to a String and adds the raw response body before returning the response.
    BOTH
  }

  private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofMillis(30000);

  private String baseUrl = null;
  private Object body;
  private String bodyJson;
  private String faultTolerantScope;
  private Feature feature;
  private String featureName = null;
  private long startNano = 0;
  private FormBody formBody;
  private SingleValueMap<String, String> headers = new SingleValueMap<>();
  private boolean isCircuitBreakerOpen = false;
  private String method = "GET";
  private ConnectionSettings mutualAuthSettings;
  private Consumer<Response> onComplete;
  private Function<Response, Object> onProcess;
  private String path = "";
  private PreferredResponseBodyType preferredResponseBodyType = PreferredResponseBodyType.INFERRED_FROM_CONTENT_TYPE;
  private SingleValueMap<String, String> queryStringParams = new SingleValueMap<>();
  private Duration timeOut;
  private String traceId;
  private String traceSpanId;

  public Request() {
    headers.put("Accept", "application/json");
    headers.put("Content-Type", "application/json");
  }

  public final Request withAccept(String accept) {
    if (accept == null) {
      headers.remove("Accept");
    } else {
      headers.put("Accept", accept);
    }

    return this;
  }

  public final String getAccept() {
    return headers.get("Accept");
  }

  public final Request withBaseUrl(String newBaseUrl) {
    this.baseUrl = newBaseUrl;
    return this;
  }

  public final String getBaseUrl() {
    return baseUrl;
  }

  public final Request withBody(Object newBody) {
    this.body = newBody;
    return this;
  }

  public final Object getBody() {
    return body;
  }

  public final Request withBodyJson(String newBodyJson) {
    this.bodyJson = newBodyJson;
    return this;
  }

  public final String getBodyJson() {
    return bodyJson;
  }

  public final Request withPreferredResponseBodyType(PreferredResponseBodyType responseBodyTypePreference) {
    this.preferredResponseBodyType = responseBodyTypePreference;
    return this;
  }

  public final PreferredResponseBodyType getPreferredResponseBodyType() {
    return preferredResponseBodyType;
  }

  public final boolean hasBody() {
    return body != null;
  }

  public final Request withContentType(String contentType) {
    if (contentType == null) {
      headers.remove("Content-Type");
    } else {
      headers.put("Content-Type", contentType);
    }

    return this;
  }

  /**
   * @return Request timeout in milliseconds
   */
  public final Duration getRequestTimeOut() {
    return timeOut == null ? DEFAULT_REQUEST_TIMEOUT : timeOut;
  }

  /**
   * Request timeout in milliseconds
   *
   * @return this
   */
  public final Request withTimeOut(Duration requestTimeOut) {
    timeOut = requestTimeOut;
    return this;
  }

  public final String getFaultTolerantScope() {
    return faultTolerantScope;
  }

  public final Request withFaultTolerantScope(String newFaultTolerantScope) {
    faultTolerantScope = newFaultTolerantScope;
    return this;
  }

  @Deprecated
  public final String getFeatureName() {
    return featureName;
  }

  @Deprecated
  public final Request withFeatureName(String featureKeyName) {
    featureName = featureKeyName;
    return this;
  }

  public final Feature getFeature() {
    return feature;
  }

  public final Request withFeature(Feature newFeature) {
    feature = newFeature;
    return this;
  }

  public final String getContentType() {
    return headers.get("Content-Type");
  }

  public final FormBody getFormBody() {
    return formBody;
  }

  public final Request withFormBody(FormBody newFormBody) {
    this.formBody = newFormBody;
    return this;
  }

  public final Request withHeader(String newHeaderKey, String newHeaderValue) {
    this.headers.put(newHeaderKey, newHeaderValue);
    return this;
  }

  public final Request withHeaders(Consumer<Map<String, String>> headerConsumer) {
    headerConsumer.accept(this.headers);
    return this;
  }

  // todo: Not sure this belongs here. Maybe move to Response. ??
  public final void setCircuitBreakerOpen(boolean circuitBreakerOpen) {
    isCircuitBreakerOpen = circuitBreakerOpen;
  }

  public final boolean isCircuitBreakerOpen() {
    return isCircuitBreakerOpen;
  }

  public final Map<String, String> getHeaders() {
    return headers;
  }

  public final Request withMethod(String newMethod) {
    this.method = newMethod;
    return this;
  }

  public final String getMethod() {
    return method;
  }

  public final ConnectionSettings getMutualAuthSettings() {
    return this.mutualAuthSettings;
  }

  public final Request withMutualAuthSettings(ConnectionSettings newMutualAuthSettings) {
    this.mutualAuthSettings = newMutualAuthSettings;
    return this;
  }

  public final Request withOnComplete(Consumer<Response> newOnComplete) {
    this.onComplete = newOnComplete;
    return this;
  }

  public final Request withPath(String newPath) {
    this.path = newPath;
    return this;
  }

  public final String getPath() {
    return path;
  }

  public final Request withQueryStringParams(Consumer<Map<String, String>> queryStringParamConsumer) {
    queryStringParamConsumer.accept(this.queryStringParams);
    return this;
  }

  public final Request withProcessor(Function<Response, Object> newOnProcess) {
    this.onProcess = newOnProcess;
    return this;
  }

  public final Request withQueryStringParams(SingleValueMap<String, String> newQueryStringParams) {
    this.queryStringParams = newQueryStringParams;
    return this;
  }

  public final Map<String, String> getQueryStringParams() {
    return queryStringParams;
  }

  public final void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public final String getTraceId() {
    return traceId;
  }

  public final void setTraceSpanId(String traceSpanId) {
    this.traceSpanId = traceSpanId;
  }

  public final String getTraceSpanId() {
    return traceSpanId;
  }

  public final Request withSpanContext(SpanContext context) {
    if (Objects.nonNull(context)) {
      this.traceId = context.toTraceId();
      this.traceSpanId = context.toSpanId();
    }

    return this;
  }

  public final long getStartNano() {
    return startNano;
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

  // Public

  public final void completed(Response currentResponse) throws RuntimeException {
    if (this.onComplete != null) {
      this.onComplete.accept(currentResponse);
    }
  }

  /**
   * Execute this request
   *
   * @return Response
   */
  public final Response execute() {
    // Get a processor that is fit for this request and process the request
    return RequestProcessorFactory.forRequest(this).process(this);
  }

  public final Object process(Response currentResponse) throws RuntimeException {
    if (this.onProcess != null) {
      return this.onProcess.apply(currentResponse);
    }

    return null;
  }

  public final void start() {
    if (startNano == 0) {
      startNano = System.nanoTime();
    }
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Request request = (Request) o;
    return Objects.equals(timeOut, request.timeOut)
        && Objects.equals(traceId, request.traceId)
        && Objects.equals(traceSpanId, request.traceSpanId)
        && Objects.equals(baseUrl, request.baseUrl)
        && Objects.equals(body, request.body)
        && Objects.equals(bodyJson, request.bodyJson)
        && Objects.equals(feature, request.feature)
        && Objects.equals(featureName, request.featureName)
        && Objects.equals(formBody, request.formBody)
        && Objects.equals(headers, request.headers)
        && Objects.equals(method, request.method)
        && Objects.equals(path, request.path)
        && Objects.equals(queryStringParams, request.queryStringParams);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(baseUrl, body, bodyJson, feature, featureName, formBody, headers, method, path, queryStringParams, timeOut, traceId, traceSpanId);
  }
}
