package com.mx.path.core.common.remote_deposits;

import com.mx.path.core.common.exception.PathRequestException;

/**
   * Exception thrown when an account has insufficient funds for a remote deposits.
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
    withHeader("ErrorSubCode", ErrorConstants.INSUFFICIENT_FUNDS_CODE);
  }
}
