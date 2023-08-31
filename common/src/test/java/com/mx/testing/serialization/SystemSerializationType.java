package com.mx.testing.serialization;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SystemSerializationType {
  private Throwable throwable;

  @Builder.Default
  private String message = "I am a test";

  @Builder.Default
  private LocalDate localDate = null;

  @Builder.Default
  private LocalDateTime localDateTime = null;

  @Builder.Default
  private Duration duration = null;

  @Builder.Default
  private Pattern pattern = null;
}
