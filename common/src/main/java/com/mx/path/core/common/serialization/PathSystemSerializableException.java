package com.mx.path.core.common.serialization;

import com.mx.path.core.common.exception.PathSystemException;

public class PathSystemSerializableException extends PathSystemException {
  public PathSystemSerializableException(String message) {
    super(message);
  }

  public PathSystemSerializableException(String message, Throwable cause) {
    super(message, cause);
  }
}
