package com.mx.testing;

import com.mx.common.connect.Response;

public class ResponseImpl extends Response<RequestImpl, ResponseImpl> {
  public ResponseImpl(RequestImpl request) {
    withRequest(request);
  }
}
