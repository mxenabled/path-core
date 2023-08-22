package com.mx.path.core.common.serialization;

import com.mx.path.core.common.exception.PathSystemException;

public class SerializationException extends PathSystemException {
  public SerializationException(String message) {
    super(message);
  }

  public SerializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
