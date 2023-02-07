package com.mx.path.gateway.connect.filters;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.connect.ConnectException;

public class HttpClientConnectException extends ConnectException {
  public HttpClientConnectException(String message, Throwable cause) {
    super(message, cause);
    withStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
    withReport(true); // todo: for now. We will probably want to change this once we nail down the http client exceptions.
  }
}
