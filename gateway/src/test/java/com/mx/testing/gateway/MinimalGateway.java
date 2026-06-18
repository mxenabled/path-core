package com.mx.testing.gateway;

import lombok.experimental.SuperBuilder;

import com.mx.path.gateway.Gateway;
import com.mx.testing.MinimalAccessor;

@SuperBuilder
public class MinimalGateway extends Gateway<MinimalAccessor> {
  public MinimalGateway() {
  }

  public MinimalGateway(String clientId) {
    super(clientId);
  }
}
