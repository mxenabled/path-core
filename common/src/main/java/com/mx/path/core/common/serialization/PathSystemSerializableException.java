package com.mx.path.core.common.serialization;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.exception.PathSystemException;

public class PathSystemSerializableException extends PathSystemException {
  @Getter
  @Setter
  private String originalType;

  public PathSystemSerializableException(String message) {
    super(message);
  }

  public PathSystemSerializableException(String message, Throwable cause) {
    super(message, cause);
  }
}
