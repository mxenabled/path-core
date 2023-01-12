package com.mx.testing.serialization;

import java.time.Duration;

import lombok.Data;

import com.mx.common.configuration.ConfigurationField;

@Data
public class ConfigurationWithDuration {
  @ConfigurationField
  private Duration duration;
}
