package com.mx.testing.accessors.proxy.id;

import com.mx.common.accessors.AccessorConfiguration;
import com.mx.testing.accessors.IdBaseAccessor;

public class IdBaseAccessorProxySingleton extends IdBaseAccessorProxy {
  private IdBaseAccessor instance;

  public IdBaseAccessorProxySingleton(AccessorConfiguration configuration,
      Class<? extends IdBaseAccessor> accessorClass) {
    super(configuration, accessorClass);
    this.instance = buildAccessor();
  }

  public IdBaseAccessorProxySingleton(AccessorConfiguration configuration,
      Class<? extends IdBaseAccessor> accessorClass, IdBaseAccessor instance) {
    super(configuration, accessorClass);
    this.instance = instance;
  }

  @Override
  public IdBaseAccessor build() {
    return instance;
  }

  @Override
  public String getScope() {
    return "singleton";
  }
}
