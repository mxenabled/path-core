package com.mx.testing.binding;

import lombok.Getter;

import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.core.common.connect.AccessorConnectionSettings;

public class ConnectionWithBoundConfiguration extends AccessorConnectionSettings {
  @Getter
  private final BasicConfigurationObj configs;

  public ConnectionWithBoundConfiguration(@Configuration BasicConfigurationObj configs) {
    this.configs = configs;
  }
}
