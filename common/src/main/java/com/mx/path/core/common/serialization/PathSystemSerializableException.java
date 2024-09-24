package com.mx.path.core.common.serialization;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.exception.PathSystemException;

/**
 * Exception to thrown on serialization system problems.
 */
public class PathSystemSerializableException extends PathSystemException {

  /**
   * -- GETTER --
   * Return original type.
   *
   * @return original type
   * -- SETTER --
   * Set original type.
   *
   * @param originalType original type to set
   */
  @Getter
  @Setter
  private String originalType;

  /**
   * Build new {@link PathSystemSerializableException} with specified parameters.
   *
   * @param message message
   */
  public PathSystemSerializableException(String message) {
    super(message);
  }

  /**
   * Build new {@link PathSystemSerializableException} with specified parameters.
   *
   * @param message message
   * @param cause cause
   */
  public PathSystemSerializableException(String message, Throwable cause) {
    super(message, cause);
  }
}
