package com.mx.path.api.connect.http;

import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilter;
import com.mx.common.exception.AccessorSystemException;

public class HttpRequest extends Request<HttpRequest, HttpResponse> {

  /**
   * Create new HttpRequest
   * @param filterChain the first RequestFilter in linked chain
   */
  public HttpRequest(RequestFilter filterChain) {
    if (filterChain == null) {
      throw new AccessorSystemException("Mis-configured HttpRequest. filterChain is null");
    }
    getHeaders().put("Accept", "application/json");
    getHeaders().put("Content-Type", "application/json");
    setFilterChain(filterChain);
  }

  /**
   * Execute this request
   * @return HttpResponse
   */
  @Override
  public HttpResponse execute() {
    HttpResponse response = new HttpResponse(this);
    getFilterChain().execute(this, response);
    return response;
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
