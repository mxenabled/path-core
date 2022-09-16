package com.mx.testing.binding;

import lombok.Getter;

import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.configuration.Configuration;
import com.mx.testing.accessors.AccountBaseAccessor;

public class AccessorWithBoundConfiguration extends AccountBaseAccessor {

  @Getter
  private final BasicConfigurationObj configurationObj;

  public AccessorWithBoundConfiguration(AccessorConfiguration configuration, @Configuration BasicConfigurationObj config) {
    super(configuration);

    this.configurationObj = config;
  }

}
