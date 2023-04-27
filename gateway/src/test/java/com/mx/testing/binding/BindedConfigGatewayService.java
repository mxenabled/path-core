package com.mx.testing.binding;

import lombok.Data;
import lombok.Getter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.core.common.configuration.ConfigurationField;
import com.mx.path.gateway.configuration.Configurable;
import com.mx.path.gateway.configuration.annotations.ClientID;
import com.mx.path.gateway.service.GatewayService;

public class BindedConfigGatewayService extends GatewayService implements Configurable {
  @Data
  public static class Config {
    @ConfigurationField("string")
    private String string;

    @ClientID
    private String clientId;
  }

  @Getter
  private Config config;

  @Getter
  private boolean initialize = false;

  public BindedConfigGatewayService(ObjectMap configurations, @Configuration Config config) {
    super(configurations);
    this.config = config;
  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  @Override
  public void initialize() {
    initialize = true;
  }
}
