package com.mx.testing.connect;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilterBase;
import com.mx.path.core.common.connect.Response;

public class TestFilterA extends RequestFilterBase {
  @Override
  public void execute(Request request, Response response) {
    response.getHeaders().put(getClass().getSimpleName(), "visited");
    next(request, response);
  }
}
