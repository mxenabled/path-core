package com.mx.testing.binding;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.Getter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.configuration.ConfigurationField;
import com.mx.path.gateway.configuration.Configurable;
import com.mx.path.gateway.configuration.ConfigurationState;
import com.mx.path.gateway.configuration.annotations.ClientID;

@Data
public class BasicConfigurationObj implements Configurable {

  @Data
  public static class SubConfigurationObj {

    @ConfigurationField("subkey1")
    private String subkey1;

  }

  @Getter
  private boolean initialized = false;

  @Getter
  private boolean validated = false;

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

  @ConfigurationField(value = "deposit", elementType = HashMap.class)
  private HashMap<String, ObjectMap> deposit;

  @ConfigurationField(value = "regex", elementType = Pattern.class)
  private List<Pattern> regex;

  @ConfigurationField(elementType = ZoneId.class)
  private List<ZoneId> zoneIds;

  @ClientID
  private String clientId;

  @Override
  public void initialize() {
    initialized = true;
  }

  @Override
  public void validate(ConfigurationState state) {
    validated = true;
  }
}
