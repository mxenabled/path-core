package com.mx.path.core.common.connect;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

import lombok.Getter;
import lombok.Setter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.core.common.collection.MultiValueMap;
import com.mx.path.core.common.collection.MultiValueMappable;
import com.mx.path.core.common.collection.SingleValueMap;
import com.mx.path.core.common.exception.PathRequestException;
import com.mx.path.core.common.http.HttpStatus;

public abstract class Response<REQ extends Request<?, ?>, RESP extends Response<?, ?>> {

  /**
   * @deprecated This makes too many assumptions about the response payload. The connection code should handle configuring the associated serializer.
   */
  @Deprecated
  private static final Gson GSON = new GsonBuilder()
      .setPrettyPrinting()
      .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
      .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
      .create();

  @Getter
  @Setter
  private int attempt;

  @Getter
  @Setter
  private String body = "";

  private MultiValueMap<String, String> cookies = new MultiValueMap<>();

  @Getter
  @Setter
  private Duration duration;

  @Getter
  @Setter
  private Exception exception;

  private MultiValueMap<String, String> headers = new MultiValueMap<>();

  private Object object;

  @Setter
  private byte[] rawBody;

  private Request<?, ?> request;

  @Getter
  @Setter
  private HttpStatus status;

  public final void finish() {
    if (duration == null) {
      request.start();
      long endNano = System.nanoTime();
      withDuration(Duration.ofNanos(endNano - request.getStartNano()));
      withAttempt(request.getAttemptCount());
    }
  }

  /**
   * @deprecated JSON deserialization will be removed in a future release. Use {@link Request#withProcessor(Function)}
   */
  @Deprecated
  public final <T> T getBodyAs(Class<T> asClass) {
    return GSON.fromJson(this.body, asClass);
  }

  public final SingleValueMap<String, String> getCookies() {
    return cookies.toSingleValueMap();
  }

  public final SingleValueMap<String, String> getHeaders() {
    if (headers == null) {
      return null;
    }
    return headers.toSingleValueMap();
  }

  public final MultiValueMap<String, String> getHeadersAsMultiValueMap() {
    return headers;
  }

  public final SingleValueMap<String, String> getHeadersAsSingleValueMap() {
    return headers.toSingleValueMap();
  }

  public final <T> T getObject(Class<T> klass) {
    return klass.isInstance(this.object) ? klass.cast(this.object) : null;
  }

  @SuppressWarnings("unchecked")
  public final <T> T getObject() {
    return (T) this.object;
  }

  public final byte[] getRawBody() {
    return this.rawBody.clone();
  }

  @SuppressWarnings("unchecked")
  public final <R extends Request<?, ?>> R getRequest() {
    return (R) this.request;
  }

  public final boolean hasBody() {
    return this.body != null && this.body.length() > 0;
  }

  public final boolean hasRawBody() {
    return this.rawBody != null && this.rawBody.length > 0;
  }

  public final void setHeaders(MultiValueMappable<String, String> multiValueMappable) {
    this.headers = new MultiValueMap<>(multiValueMappable);
  }

  @SuppressWarnings("unchecked")
  public final RESP throwException() {
    if (Objects.nonNull(exception)) {
      if (exception instanceof PathRequestException) {
        throw (PathRequestException) exception;
      }

      throw new ResponseProcessingException(exception);
    }
    return (RESP) this;
  }

  @SuppressWarnings("unchecked")
  public final RESP withAttempt(int newAttempt) {
    setAttempt(newAttempt);
    return (RESP) this;
  }

  @SuppressWarnings("unchecked")
  public final RESP withBody(String newBody) {
    setBody(newBody);
    return (RESP) this;
  }

  @SuppressWarnings("unchecked")
  public final RESP withCookies(MultiValueMappable<String, String> newCookies) {
    this.cookies = new MultiValueMap<>(newCookies);
    return (RESP) this;
  }

  @SuppressWarnings("unchecked")
  public final RESP withDuration(Duration newDuration) {
    this.duration = newDuration;
    return (RESP) this;
  }

  @SuppressWarnings("unchecked")
  public final RESP withException(Exception newException) {
    this.exception = newException;
    return (RESP) this;
  }

  @SuppressWarnings("unchecked")
  public final RESP withHeaders(MultiValueMap<String, String> newHeaders) {
    this.headers = newHeaders;
    return (RESP) this;
  }

  @SuppressWarnings("unchecked")
  public final RESP withObject(Object newObject) {
    this.object = newObject;
    return (RESP) this;
  }

  @SuppressWarnings("unchecked")
  public final RESP withRawData(byte[] newRawBody) {
    this.rawBody = newRawBody.clone();
    return (RESP) this;
  }

  @SuppressWarnings("unchecked")
  public final RESP withRequest(Request<?, ?> newRequest) {
    this.request = newRequest;
    return (RESP) this;
  }

  @SuppressWarnings("unchecked")
  public final RESP withStatus(HttpStatus newStatus) {
    this.status = newStatus;
    return (RESP) this;
  }
}
