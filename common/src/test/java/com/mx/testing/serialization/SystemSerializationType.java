package com.mx.testing.serialization;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SystemSerializationType {
  private Throwable throwable;

  @Builder.Default
  private String message = "I am a test";
}
