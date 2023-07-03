package com.mx.testing.binding;

import lombok.Getter;

import com.mx.path.core.common.configuration.Configuration;
import com.mx.testing.accessors.AccountBaseAccessor;

public class AccessorWithNoConfigWithBoundConfiguration extends AccountBaseAccessor {

  @Getter
  private final BasicConfigurationObj configurationObj;

  public AccessorWithNoConfigWithBoundConfiguration(@Configuration BasicConfigurationObj config) {
    this.configurationObj = config;
  }
}
