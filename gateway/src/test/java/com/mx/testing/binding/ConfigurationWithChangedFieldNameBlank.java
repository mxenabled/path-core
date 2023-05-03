package com.mx.testing.binding;

import lombok.Data;

import com.mx.common.configuration.ConfigurationField;

@Data
public class ConfigurationWithChangedFieldNameBlank {
  @ConfigurationField(value = "", required = true)
  private String klass;
}
