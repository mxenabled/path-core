package com.mx.testing.binding;

import lombok.Getter;

import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.gateway.configuration.annotations.Connection;
import com.mx.testing.accessors.AccountBaseAccessor;

public class AccessorWithNoConfigWithBoundConnections extends AccountBaseAccessor {

  @Getter
  private final BasicConfigurationObj configs;

  @Getter
  private final ConnectionWithBoundConfiguration connection1;

  @Getter
  private final ConnectionWithBoundConfiguration connection2;

  public AccessorWithNoConfigWithBoundConnections(
      @Configuration BasicConfigurationObj configs,
      @Connection("connection1") ConnectionWithBoundConfiguration connection1,
      @Connection("connection2") ConnectionWithBoundConfiguration connection2) {
    this.configs = configs;
    this.connection1 = connection1;
    this.connection2 = connection2;
  }
}
