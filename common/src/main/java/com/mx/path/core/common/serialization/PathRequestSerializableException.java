package com.mx.path.core.common.serialization;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown on request serialization problems.
 */
public class PathRequestSerializableException extends PathRequestException {

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
}
