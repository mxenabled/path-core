package com.mx.testing.gateway;

import lombok.experimental.SuperBuilder;

import com.mx.path.gateway.Gateway;
import com.mx.path.gateway.configuration.AccessorProxyMap;
import com.mx.testing.accessors.AccountBaseAccessor;
import com.mx.testing.accessors.BaseAccessor;
import com.mx.testing.accessors.IdBaseAccessor;
import com.mx.testing.accessors.proxy.BaseAccessorProxySingleton;
import com.mx.testing.accessors.proxy.account.AccountBaseAccessorProxySingleton;
import com.mx.testing.accessors.proxy.id.IdBaseAccessorProxySingleton;

@SuperBuilder
public class BaseGateway extends Gateway<BaseAccessor> {
  static {
    AccessorProxyMap.add("singleton", BaseAccessor.class, BaseAccessorProxySingleton.class);
    AccessorProxyMap.add("singleton", AccountBaseAccessor.class, AccountBaseAccessorProxySingleton.class);
    AccessorProxyMap.add("singleton", IdBaseAccessor.class, IdBaseAccessorProxySingleton.class);
  }

  public BaseGateway() {
  }

  public BaseGateway(String clientId) {
    super(clientId);
  }
}
