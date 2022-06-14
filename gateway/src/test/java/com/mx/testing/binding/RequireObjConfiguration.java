package com.mx.testing.binding;

import lombok.Data;

import com.mx.path.gateway.configuration.annotations.ConfigurationField;

@Data
public class RequireObjConfiguration {

  @Data
  public static class SubConfigurationObj {

    @ConfigurationField(value = "subkey1", required = true)
    private String subkey1;

  }

  @ConfigurationField(value = "subConfig", required = true)
  private SubConfigurationObj subConfig;
}
