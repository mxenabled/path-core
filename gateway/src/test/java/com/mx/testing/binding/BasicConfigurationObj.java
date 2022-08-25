package com.mx.testing.binding;

import java.util.List;

import lombok.Data;

import com.mx.common.configuration.ConfigurationField;
import com.mx.path.gateway.configuration.annotations.ClientID;

@Data
public class BasicConfigurationObj {

  @Data
  public static class SubConfigurationObj {

    @ConfigurationField("subkey1")
    private String subkey1;

  }

  @ConfigurationField("key1")
  private String key1;

  @ConfigurationField("key2")
  private Integer key2;

  @ConfigurationField(value = "subConfig")
  private SubConfigurationObj subConfig;

  @ConfigurationField(value = "array1", elementType = String.class)
  private List<String> array1;

  @ConfigurationField(value = "complexArray1", elementType = SubConfigurationObj.class)
  private List<SubConfigurationObj> complexArray1;

  @ClientID
  private String clientId;

}
