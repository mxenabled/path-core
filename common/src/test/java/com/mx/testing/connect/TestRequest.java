package com.mx.testing.connect;

import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilter;

public class TestRequest extends Request<TestRequest, TestResponse> {
  public TestRequest(RequestFilter requestFilter) {
    super(requestFilter);
  }

  @Override
  public TestResponse execute() {
    return null;
  }
}
