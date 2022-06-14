package com.mx.testing.binding;

import lombok.Getter;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.path.gateway.configuration.annotations.Configuration;

public class AccessorWithBoundConfiguration extends AccountBaseAccessor {

  @Getter
  private final BasicConfigurationObj configurationObj;

  public AccessorWithBoundConfiguration(AccessorConfiguration configuration, @Configuration BasicConfigurationObj config) {
    super(configuration);

    this.configurationObj = config;
  }

}
