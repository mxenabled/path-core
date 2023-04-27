package com.mx.path.gateway.connect.filter;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.connect.ConnectException;

public class HttpClientConnectException extends ConnectException {
  public HttpClientConnectException(String message, Throwable cause) {
    super(message, cause);
    withStatus(PathResponseStatus.UPSTREAM_SERVICE_UNAVAILABLE);
    withReport(true); // todo: for now. We will probably want to change this once we nail down the http client exceptions.
  }
}
