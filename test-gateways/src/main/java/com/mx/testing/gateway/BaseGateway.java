package com.mx.testing.gateway;

import lombok.experimental.SuperBuilder;

import com.mx.path.core.common.gateway.GatewayBaseClass;
import com.mx.path.gateway.Gateway;
import com.mx.path.gateway.configuration.RootGateway;
import com.mx.testing.accessors.BaseAccessor;

@RootGateway
@SuperBuilder
@GatewayBaseClass(target = BaseAccessor.class, namespace = "com.mx.testing.gateway.api", className = "Gateway")
public class BaseGateway extends Gateway<BaseAccessor> {
  public BaseGateway() {
  }

  public BaseGateway(String clientId) {
    super(clientId);
  }
}
