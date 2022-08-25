package com.mx.testing.binding;

import lombok.Getter;

import com.mx.common.configuration.Configuration;
import com.mx.common.connect.AccessorConnectionSettings;
import com.mx.path.gateway.configuration.annotations.ClientID;

public class ConnectionWithBoundConfiguration extends AccessorConnectionSettings {
  @Getter
  private final BasicConfigurationObj configs;

  @Getter
  private String clientId;

  public ConnectionWithBoundConfiguration(@Configuration BasicConfigurationObj configs, @ClientID String clientId) {
    this.configs = configs;
    this.clientId = clientId;
  }
}
