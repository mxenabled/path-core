// ---------------------------------------------------------------------------------------------------------------------
//   GENERATED FILE - ** Do not edit **
// ---------------------------------------------------------------------------------------------------------------------
package com.mx.testing.accessors.proxy.account;

import com.mx.path.core.common.accessor.AccessorConfiguration;
import com.mx.testing.accessors.AccountBaseAccessor;

public class AccountBaseAccessorProxySingleton extends AccountBaseAccessorProxy {
  private AccountBaseAccessor instance;

  public AccountBaseAccessorProxySingleton(AccessorConfiguration configuration,
      Class<? extends AccountBaseAccessor> accessorClass) {
    super(configuration, accessorClass);
    this.instance = buildAccessor();
  }

  public AccountBaseAccessorProxySingleton(AccessorConfiguration configuration,
      Class<? extends AccountBaseAccessor> accessorClass, AccountBaseAccessor instance) {
    super(configuration, accessorClass);
    this.instance = instance;
  }

  @Override
  public AccountBaseAccessor build() {
    return instance;
  }

  @Override
  public String getScope() {
    return "singleton";
  }
}
