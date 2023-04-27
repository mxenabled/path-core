package com.mx.testing.binding;

import lombok.Data;

import com.mx.path.core.common.configuration.ConfigurationField;

@Data
public class ConfigurationWithChangedFieldName {
  @ConfigurationField(value = "class", required = true)
  private String klass;
}
