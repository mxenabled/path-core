package com.mx.testing.binding;

import lombok.Getter;

import com.mx.accessors.AccessorConnection;
import com.mx.path.gateway.configuration.annotations.ClientID;
import com.mx.path.gateway.configuration.annotations.Configuration;

public class ConnectionWithBoundConfiguration extends AccessorConnection {
  @Getter
  private final BasicConfigurationObj configs;

  @Getter
  private String clientId;

  public ConnectionWithBoundConfiguration(@Configuration BasicConfigurationObj configs, @ClientID String clientId) {
    this.configs = configs;
    this.clientId = clientId;
  }
}
