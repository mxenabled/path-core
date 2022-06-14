package com.mx.testing.binding;

import java.util.List;

import lombok.Data;

import com.mx.path.gateway.configuration.annotations.ConfigurationField;

@Data
public class RequireArrayConfiguration {

  @Data
  public static class SubConfigurationObj {

    @ConfigurationField(value = "subkey1", required = true)
    private String subkey1;

  }

  @ConfigurationField(value = "stringArray", elementType = String.class, required = true)
  private List<String> stringArray;

  @ConfigurationField(value = "objArray", elementType = SubConfigurationObj.class, required = true)
  private List<SubConfigurationObj> objArray;

}
