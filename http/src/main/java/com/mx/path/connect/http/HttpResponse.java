package com.mx.path.connect.http;

import com.mx.path.core.common.connect.Response;

/**
 * Represents an HTTP response.
 */
public class HttpResponse extends Response<HttpRequest, HttpResponse> {

  /**
   * Default constructor for creating an empty {@link HttpResponse}.
   */
  public HttpResponse() {
  }

  /**
   * Build {@link HttpResponse} with specified {@link HttpRequest}.
   *
   * @param request {@link HttpRequest} associated with this response
   */
  public HttpResponse(HttpRequest request) {
    super();
    withRequest(request);
  }
}
