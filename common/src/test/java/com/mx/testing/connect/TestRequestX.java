package com.mx.testing.connect;

import com.mx.common.connect.Request;

public class TestRequestX extends Request<TestRequestX, TestResponseX> {
  public TestRequestX() {
    super(null);
  }

  @Override
  public TestResponseX execute() {
    return null;
  }
}
