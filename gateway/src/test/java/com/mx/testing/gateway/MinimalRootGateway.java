package com.mx.testing.gateway;

import lombok.experimental.SuperBuilder;

import com.mx.path.gateway.Gateway;
import com.mx.path.gateway.configuration.RootGateway;
import com.mx.testing.MinimalAccessor;

@SuperBuilder
@RootGateway
public class MinimalRootGateway extends Gateway<MinimalAccessor> {
  public MinimalRootGateway() {
  }
}
