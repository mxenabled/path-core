package com.mx.testing.connect;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilter;

public class TestRequest extends Request<TestRequest, TestResponse> {
  public TestRequest(RequestFilter requestFilter) {
    super(requestFilter);
  }

  @Override
  public TestResponse execute() {
    return null;
  }
}
