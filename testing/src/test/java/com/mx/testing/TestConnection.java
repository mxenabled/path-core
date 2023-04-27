package com.mx.testing;

import java.util.Collections;
import java.util.List;

import com.mx.path.core.common.accessor.AccessorConnectionBase;
import com.mx.path.core.common.connect.RequestFilter;

public class TestConnection extends AccessorConnectionBase<TestConnection.Request> {
  @Override
  public List<RequestFilter> connectionRequestFilters() {
    return Collections.emptyList();
  }

  @Override
  public Request request(String path) {
    return null;
  }

  public static class Response extends com.mx.path.core.common.connect.Response<Request, Response> {
  }

  public static class Request extends com.mx.path.core.common.connect.Request<Request, Response> {
    public Request() {
      super(null);
    }

    @Override
    public Response execute() {
      return null;
    }
  }

}
