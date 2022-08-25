package com.mx.testing.serialization;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ClassWithTimestamps {
  private LocalDateTime localDateTime;
}
