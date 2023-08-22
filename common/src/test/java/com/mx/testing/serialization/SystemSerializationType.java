package com.mx.testing.serialization;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SystemSerializationType {
  private Throwable throwable;
}
