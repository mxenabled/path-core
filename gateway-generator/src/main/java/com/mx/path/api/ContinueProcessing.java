package com.mx.path.api;

/**
 * Custom exception class used to indicate that processing should continue after exception.
 */
public class ContinueProcessing extends RuntimeException {

  /**
   * Build new {@link ContinueProcessing} exception with specified message.
   *
   * @param message exception message to be set
   */
  public ContinueProcessing(String message) {
    super(message);
  }
}
