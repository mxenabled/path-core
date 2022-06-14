package com.mx.path.gateway.net;

import java.util.Objects;
import java.util.function.Supplier;

import com.mx.common.collections.MultiValueMap;
import com.mx.common.collections.SingleValueMap;
import com.mx.common.http.HttpStatus;
import com.mx.path.gateway.util.MdxApiException;

public class Response {

  public static final Integer NANO_TO_SECONDS = 1000000;

  private byte[] rawBody;
  private String body = "";
  private MultiValueMap<String, String> cookies = new MultiValueMap<>();
  private Long duration;
  private Exception exception;
  private MultiValueMap<String, String> headers = new MultiValueMap<>();
  private Request request;
  private HttpStatus status;
  private Object object;

  public Response() {
  }

  public Response(Request request) {
    this.request = request;
  }

  // Getter/Setters

  public final Response withBody(String newBody) {
    this.body = newBody;
    return this;
  }

  public final Response withRawData(byte[] newRawBody) {
    this.rawBody = newRawBody.clone();
    return this;
  }

  public final boolean hasBody() {
    return this.body != null && this.body.length() > 0;
  }

  public final String getBody() {
    return this.body;
  }

  public final boolean hasRawBody() {
    return this.rawBody != null && this.rawBody.length > 0;
  }

  public final byte[] getRawBody() {
    return this.rawBody.clone();
  }

  public final <T> T getBodyAs(Class<T> asClass) {
    return Serializer.get().fromJson(this.body, asClass);
  }

  public final Response withCookies(MultiValueMap<String, String> newCookies) {
    this.cookies = newCookies;
    return this;
  }

  public final SingleValueMap<String, String> getCookies() {
    return cookies.toSingleValueMap();
  }

  public final Response withDuration(long newDuration) {
    this.duration = newDuration;
    return this;
  }

  public final Long getDuration() {
    return this.duration;
  }

  public final Response withException(Exception newException) {
    this.exception = newException;
    return this;
  }

  public final Exception getException() {
    return this.exception;
  }

  public final Response withHeaders(MultiValueMap<String, String> newHeaders) {
    this.headers = newHeaders;
    return this;
  }

  public final SingleValueMap<String, String> getHeaders() {
    if (headers == null) {
      return null;
    }
    return headers.toSingleValueMap();
  }

  public final Response withObject(Object newObject) {
    this.object = newObject;
    return this;
  }

  public final <T> T getObject(Class<T> klass) {
    return klass.isInstance(this.object) ? klass.cast(this.object) : null;
  }

  public final Response withRequest(Request newRequest) {
    this.request = newRequest;
    return this;
  }

  public final Request getRequest() {
    return this.request;
  }

  public final Response withStatus(HttpStatus newStatus) {
    this.status = newStatus;
    return this;
  }

  public final HttpStatus getStatus() {
    return this.status;
  }

  // Public
  public final void checkStatus() throws MdxApiException {
    if (exception != null || getStatus() == null) {
      throw new MdxApiException("Request threw an exception", HttpStatus.INTERNAL_SERVER_ERROR, true, exception);
    }
    if (getStatus().isError()) {
      throw new MdxApiException(getStatus());
    }
  }

  // The execFunc was expected to throw MDXApi Exception with its own implementation
  public final void checkStatus(Supplier execFunc) throws MdxApiException {
    if (exception != null || getStatus() == null) {
      throw new MdxApiException("Request threw an exception", HttpStatus.INTERNAL_SERVER_ERROR, true, exception);
    }
    if (getStatus().isError()) {
      execFunc.get();
    }
  }

  public final void throwException() throws MdxApiException {
    if (Objects.nonNull(exception)) {
      if (exception instanceof MdxApiException) {
        throw (MdxApiException) exception;
      }

      throw new MdxApiException("Unprocessed request exception", HttpStatus.INTERNAL_SERVER_ERROR, true, exception);
    }
  }

  public final void finish() {
    if (duration == null) {
      request.start();
      long endNano = System.nanoTime();
      withDuration((endNano - request.getStartNano()) / NANO_TO_SECONDS);
    }
  }
}
