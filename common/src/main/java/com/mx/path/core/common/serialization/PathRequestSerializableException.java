package com.mx.path.core.common.serialization;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.exception.PathRequestException;

public class PathRequestSerializableException extends PathRequestException {
  @Getter
  @Setter
  private String originalType;
}
