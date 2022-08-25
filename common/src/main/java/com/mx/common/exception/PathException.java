package com.mx.common.exception;

/**
 * Base for all exceptions thrown by Path libraries
 */
public class PathException extends RuntimeException {
  public PathException() {
    super();
  }

  public PathException(String message) {
    super(message);
  }

  public PathException(Throwable cause) {
    super(cause);
  }

  public PathException(String message, Throwable cause) {
    super(message, cause);
  }
}
