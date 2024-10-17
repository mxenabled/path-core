package com.mx.path.core.common.remote_deposits;

import com.mx.path.core.common.exception.PathRequestException;
import com.mx.path.core.common.request.Feature;

/**
   * Exception thrown when an account has insufficient funds for Account Transfers.
 */
public class InsufficientFundsException extends PathRequestException {

  public InsufficientFundsException() {
    super();
    initialize();
  }

  public InsufficientFundsException(String message, Throwable cause) {
    super(message, cause);
    initialize();
  }

  // Common initialization logic
  private void initialize() {
    setUserMessage(ErrorConstants.INSUFFICIENT_FUNDS_MESSAGE);
    setCode(ErrorConstants.INSUFFICIENT_FUNDS_CODE);
    withHeader("X-MX-Feature-Error-Code", ErrorConstants.INSUFFICIENT_FUNDS_CODE);
    withHeader("feature", Feature.REMOTE_DEPOSITS.toString());
  }
}
