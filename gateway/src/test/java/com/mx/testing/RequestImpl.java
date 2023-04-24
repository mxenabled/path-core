package com.mx.testing;

import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilter;
import com.mx.common.connect.Response;

public class RequestImpl extends Request<RequestImpl, ResponseImpl> {
  public RequestImpl() {
    super(new RequestFilter() {
      @Override
      public void execute(Request request, Response response) {
      }

      @Override
      public RequestFilter getNext() {
        return null;
      }

      @Override
      public void setNext(RequestFilter filter) {
      }
    });
  }

  @Override
  public ResponseImpl execute() {
    return new ResponseImpl(this);
  }
}
