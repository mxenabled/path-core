package com.mx.path.core.common.connect;

import com.mx.path.core.common.accessor.AccessorSystemException;

/**
 * Wraps unexpected exception type thrown during Response processing.
 */
public final class ResponseProcessingException extends AccessorSystemException {

  /**
   * Build new {@link ResponseProcessingException} with specified cause.
   *
   * @param cause cause
   */
  public ResponseProcessingException(Throwable cause) {
    super("Unexpected response exception thrown", cause);
  }
}
