package com.mx.path.core.common.compression;

import com.mx.path.core.common.exception.PathSystemException;

/**
 * Throw on compression problems.
 */
public class CompressionServiceException extends PathSystemException {

  /**
   * Build new {@link CompressionServiceException} with description message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public CompressionServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
