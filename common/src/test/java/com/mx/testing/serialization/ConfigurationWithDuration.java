package com.mx.testing.serialization;

import java.time.Duration;

import lombok.Data;

import com.mx.path.core.common.configuration.ConfigurationField;

@Data
public class ConfigurationWithDuration {
  @ConfigurationField
  private Duration duration;
}
