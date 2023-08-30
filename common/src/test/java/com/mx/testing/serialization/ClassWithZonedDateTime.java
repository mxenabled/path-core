package com.mx.testing.serialization;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class ClassWithZonedDateTime {
  private ZonedDateTime zonedDateTime;
}
