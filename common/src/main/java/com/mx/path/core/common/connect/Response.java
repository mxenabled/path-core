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

/**
 * Base class for responses.
 *
 * @param <REQ> request type
 * @param <RESP> response type
 */
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

  /**
   * -- GETTER --
   * Return attempt.
   *
   * @return attempt
   * -- SETTER --
   * Set attempt.
   *
   * @param attempt attempt to set
   */
  @Getter
  @Setter
  private int attempt;

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
  private String body = "";

  private MultiValueMap<String, String> cookies = new MultiValueMap<>();

  /**
   * -- GETTER --
   * Return duration.
   *
   * @return duration
   * -- SETTER --
   * Set duration.
   *
   * @param duration duration to set
   */
  @Getter
  @Setter
  private Duration duration;

  /**
   * -- GETTER --
   * Return exception.
   *
   * @return exception
   * -- SETTER --
   * Set exception.
   *
   * @param exception exception to set
   */
  @Getter
  @Setter
  private Exception exception;

  private MultiValueMap<String, String> headers = new MultiValueMap<>();

  private Object object;

  /**
   * -- SETTER --
   * Set raw body.
   *
   * @param rawBody raw body to set
   */
  @Setter
  private byte[] rawBody;

  private Request<?, ?> request;

  /**
   * -- GETTER --
   * Return status.
   *
   * @return status
   * -- SETTER --
   * Set status.
   *
   * @param status status to set
   */
  @Getter
  @Setter
  private HttpStatus status;

  /**
   * Attempt to finish request.
   */
  public final void finish() {
    if (duration == null) {
      request.start();
      long endNano = System.nanoTime();
      withDuration(Duration.ofNanos(endNano - request.getStartNano()));
      withAttempt(request.getAttemptCount());
    }
  }

  /**
   * @param asClass class of returned object
   * @param <T> type of returned object
   * @return deserialized object
   * @deprecated JSON deserialization will be removed in a future release. Use {@link Request#withProcessor(Function)}
   */
  @Deprecated
  public final <T> T getBodyAs(Class<T> asClass) {
    return GSON.fromJson(this.body, asClass);
  }

  /**
   * @return cookies
   */
  public final SingleValueMap<String, String> getCookies() {
    return cookies.toSingleValueMap();
  }

  /**
   * @return headers as single value map
   */
  public final SingleValueMap<String, String> getHeaders() {
    if (headers == null) {
      return null;
    }
    return headers.toSingleValueMap();
  }

  /**
   * @return headers as multi value map
   */
  public final MultiValueMap<String, String> getHeadersAsMultiValueMap() {
    return headers;
  }

  /**
   * Alias for {@link #getHeaders()}.
   *
   * @return headers
   */
  public final SingleValueMap<String, String> getHeadersAsSingleValueMap() {
    return headers.toSingleValueMap();
  }

  /**
   * Return object.
   *
   * @param klass class of object
   * @return object
   * @param <T> object type
   */
  public final <T> T getObject(Class<T> klass) {
    return klass.isInstance(this.object) ? klass.cast(this.object) : null;
  }

  /**
   * Return object.
   *
   * @return object
   * @param <T> object type
   */
  @SuppressWarnings("unchecked")
  public final <T> T getObject() {
    return (T) this.object;
  }

  /**
   * Return raw body.
   *
   * @return raw body
   */
  public final byte[] getRawBody() {
    return this.rawBody.clone();
  }

  /**
   * Return request.
   *
   * @return request
   * @param <R> request type
   */
  @SuppressWarnings("unchecked")
  public final <R extends Request<?, ?>> R getRequest() {
    return (R) this.request;
  }

  /**
   * Return body.
   *
   * @return body
   */
  public final boolean hasBody() {
    return this.body != null && this.body.length() > 0;
  }

  /**
   * Check if response has raw body.
   *
   * @return true if response has raw body
   */
  public final boolean hasRawBody() {
    return this.rawBody != null && this.rawBody.length > 0;
  }

  /**
   * Set headers.
   *
   * @param multiValueMappable map with headers to set
   */
  public final void setHeaders(MultiValueMappable<String, String> multiValueMappable) {
    this.headers = new MultiValueMap<>(multiValueMappable);
  }

  /**
   * Throw exception if not null.
   *
   * @return self
   */
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

  /**
   * Set attempt.
   *
   * @param newAttempt attempt to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final RESP withAttempt(int newAttempt) {
    setAttempt(newAttempt);
    return (RESP) this;
  }

  /**
   * Set body.
   *
   * @param newBody body to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final RESP withBody(String newBody) {
    setBody(newBody);
    return (RESP) this;
  }

  /**
   * Set cookies.
   *
   * @param newCookies cookies to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final RESP withCookies(MultiValueMappable<String, String> newCookies) {
    this.cookies = new MultiValueMap<>(newCookies);
    return (RESP) this;
  }

  /**
   * Set duration.
   *
   * @param newDuration duration to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final RESP withDuration(Duration newDuration) {
    this.duration = newDuration;
    return (RESP) this;
  }

  /**
   * Set exception.
   *
   * @param newException exception to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final RESP withException(Exception newException) {
    this.exception = newException;
    return (RESP) this;
  }

  /**
   * Set headers.
   *
   * @param newHeaders headers to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final RESP withHeaders(MultiValueMap<String, String> newHeaders) {
    this.headers = newHeaders;
    return (RESP) this;
  }

  /**
   * Set object.
   *
   * @param newObject object to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final RESP withObject(Object newObject) {
    this.object = newObject;
    return (RESP) this;
  }

  /**
   * Set raw data body.
   *
   * @param newRawBody raw data body to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final RESP withRawData(byte[] newRawBody) {
    this.rawBody = newRawBody.clone();
    return (RESP) this;
  }

  /**
   * Set request.
   *
   * @param newRequest request to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final RESP withRequest(Request<?, ?> newRequest) {
    this.request = newRequest;
    return (RESP) this;
  }

  /**
   * Set status.
   *
   * @param newStatus status to set
   * @return self
   */
  @SuppressWarnings("unchecked")
  public final RESP withStatus(HttpStatus newStatus) {
    this.status = newStatus;
    return (RESP) this;
  }
}
