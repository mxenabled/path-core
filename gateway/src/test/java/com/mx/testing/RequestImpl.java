package com.mx.testing;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilter;
import com.mx.path.core.common.connect.Response;

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

  @Override
  public ResponseImpl newResponse() {
    return null;
  }
}
