package com.mx.path.api.connect.http;

import com.mx.common.connect.Response;

public class HttpResponse extends Response<HttpRequest, HttpResponse> {
  public HttpResponse() {
  }

  public HttpResponse(HttpRequest request) {
    super();
    withRequest(request);
  }
}
