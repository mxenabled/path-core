package com.mx.testing.binding;

import lombok.Data;
import lombok.Getter;

import com.mx.common.collections.ObjectMap;
import com.mx.common.configuration.Configuration;
import com.mx.common.configuration.ConfigurationField;
import com.mx.path.gateway.configuration.annotations.ClientID;
import com.mx.path.gateway.service.GatewayService;

public class BindedConfigGatewayService extends GatewayService {
  @Data
  public static class Config {
    @ConfigurationField("string")
    private String string;

    @ClientID
    private String clientId;
  }

  @Getter
  private Config config;

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
}
