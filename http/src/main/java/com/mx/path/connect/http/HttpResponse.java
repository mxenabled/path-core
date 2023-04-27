package com.mx.path.connect.http;

import com.mx.path.core.common.connect.Response;

public class HttpResponse extends Response<HttpRequest, HttpResponse> {
  public HttpResponse() {
  }

  public HttpResponse(HttpRequest request) {
    super();
    withRequest(request);
  }
}
