package com.mx.testing.serialization;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.regex.Pattern;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SystemSerializationType {
  private Throwable throwable;

  private Duration duration;

  private LocalDate localDate;

  private LocalDateTime localDateTime;

  @Builder.Default
  private String message = "I am a test";

  private OffsetDateTime offsetDateTime;

  private Pattern pattern;

  private ZonedDateTime zonedDateTime;
}
