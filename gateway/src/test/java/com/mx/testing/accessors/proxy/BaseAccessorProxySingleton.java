// ---------------------------------------------------------------------------------------------------------------------
//   GENERATED FILE - ** Do not edit **
// ---------------------------------------------------------------------------------------------------------------------
package com.mx.testing.accessors.proxy;

import com.mx.path.core.common.accessor.RootAccessor;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.testing.accessors.BaseAccessor;

@RootAccessor
public class BaseAccessorProxySingleton extends BaseAccessorProxy {
  private BaseAccessor instance;

  public BaseAccessorProxySingleton(AccessorConfiguration configuration,
      Class<? extends BaseAccessor> accessorClass) {
    super(configuration, accessorClass);
    this.instance = buildAccessor();
  }

  public BaseAccessorProxySingleton(AccessorConfiguration configuration,
      Class<? extends BaseAccessor> accessorClass, BaseAccessor instance) {
    super(configuration, accessorClass);
    this.instance = instance;
  }

  @Override
  public BaseAccessor build() {
    return instance;
  }

  @Override
  public String getScope() {
    return "singleton";
  }
}
