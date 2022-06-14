package com.mx.testing.binding;

import lombok.Data;

import com.mx.path.gateway.configuration.annotations.ConfigurationField;

@Data
public class RequireStringFieldConfiguration {

  @ConfigurationField(value = "key1", required = true)
  private String key1;

}
