package com.mx.testing.binding;

import lombok.Getter;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.path.gateway.configuration.annotations.Configuration;
import com.mx.path.gateway.configuration.annotations.Connection;

public class AccessorWithBoundConnections extends AccountBaseAccessor {

  @Getter
  private final BasicConfigurationObj configs;

  @Getter
  private final ConnectionWithBoundConfiguration connection1;

  @Getter
  private final ConnectionWithBoundConfiguration connection2;

  public AccessorWithBoundConnections(AccessorConfiguration configuration,
      @Configuration BasicConfigurationObj configs,
      @Connection("connection1") ConnectionWithBoundConfiguration connection1,
      @Connection("connection2") ConnectionWithBoundConfiguration connection2) {
    super(configuration);
    this.configs = configs;
    this.connection1 = connection1;
    this.connection2 = connection2;
  }
}
