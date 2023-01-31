package com.mx.testing.gateway;

import lombok.experimental.SuperBuilder;

import com.mx.path.gateway.Gateway;
import com.mx.path.gateway.configuration.AccessorProxyMap;
import com.mx.path.gateway.configuration.RootGateway;
import com.mx.testing.accessors.BaseAccessor;
import com.mx.testing.accessors.proxy.BaseAccessorProxySingleton;

@RootGateway
@SuperBuilder
public class BaseGateway extends Gateway<BaseAccessor> {

  static {
    AccessorProxyMap.add("singleton", BaseAccessor.class, BaseAccessorProxySingleton.class);
  }

  public BaseGateway() {
  }

  public BaseGateway(String clientId) {
    super(clientId);
  }
}
