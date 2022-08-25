package com.mx.common.connect;

public interface RequestFilter {

  // Public Methods

  void execute(Request request, Response response);

  RequestFilter getNext();

  default void next(Request request, Response response) {
    if (getNext() != null) {
      getNext().execute(request, response);
    }
  }

  void setNext(RequestFilter filter);
}
