package com.mx.path.connect.http;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilter;

/**
 * Represents an HTTP request.
 */
public class HttpRequest extends Request<HttpRequest, HttpResponse> {

  /**
   * Create new HttpRequest
   * @param filterChain the first RequestFilter in linked chain
   */
  public HttpRequest(RequestFilter filterChain) {
    super(filterChain);

    getHeaders().put("Accept", "application/json");
    getHeaders().put("Content-Type", "application/json");
  }

  /**
   * Create new instance of HttpResponse
   *
   * @return new response instance
   */
  @Override
  public HttpResponse newResponse() {
    return new HttpResponse(this);
  }

  /**
   * Execute request as a GET
   * @return HttpResponse
   */
  public HttpResponse get() {
    setMethod("GET");
    return execute();
  }

  /**
   * Execute request as a PUT
   * @return HttpRequest
   */
  public HttpResponse put() {
    setMethod("PUT");
    return execute();
  }

  /**
   * Execute request as a DELETE
   * @return HttpRequest
   */
  public HttpResponse delete() {
    setMethod("DELETE");
    return execute();
  }

  /**
   * Execute request as a PATCH
   * @return HttpRequest
   */
  public HttpResponse patch() {
    setMethod("PATCH");
    return execute();
  }

  /**
   * Execute request as a POST
   * @return HttpRequest
   */
  public HttpResponse post() {
    setMethod("POST");
    return execute();
  }
}
