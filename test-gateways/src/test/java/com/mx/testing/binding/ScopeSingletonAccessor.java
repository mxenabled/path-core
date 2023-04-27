package com.mx.testing.binding;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.MaxScope;
import com.mx.testing.accessors.AccountBaseAccessor;

@MaxScope(AccessorScope.SINGLETON)
public class ScopeSingletonAccessor extends AccountBaseAccessor {
  public ScopeSingletonAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }
}
