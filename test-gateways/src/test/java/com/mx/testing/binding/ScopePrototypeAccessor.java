package com.mx.testing.binding;

import com.mx.path.core.common.accessor.AccessorConfiguration;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.MaxScope;
import com.mx.testing.accessors.AccountBaseAccessor;

@MaxScope(AccessorScope.PROTOTYPE)
public class ScopePrototypeAccessor extends AccountBaseAccessor {
  public ScopePrototypeAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }
}
