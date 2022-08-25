package com.mx.common.connect;

import lombok.Getter;
import lombok.Setter;

import com.mx.common.exception.PathException;
import com.mx.common.http.HttpStatus;

public class ConnectException extends PathException {
  private static final long serialVersionUID = 1L;

  @Getter
  @Setter
  private HttpStatus status;
  @Getter
  @Setter
  private String clientId;
  @Getter
  @Setter
  private String code;
  @Getter
  @Setter
  private String endpointKey;
  @Getter
  @Setter
  private String errorKey;
  @Getter
  @Setter
  private String userMessage;
  @Setter
  private boolean report;
  @Getter
  @Setter
  private String reason;
  @Setter
  private String message;
  @Getter
  @Setter
  private String errorTitle;

  public ConnectException(String message, HttpStatus status, boolean reportError, Throwable cause) {
    super(message, cause);
    setStatus(status);
    setReport(reportError);
  }

  public ConnectException(HttpStatus status) {
    super();
    setStatus(status);
  }

  public ConnectException(Throwable cause) {
    super(cause);
  }

}
