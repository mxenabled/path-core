package com.mx.testing.binding;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.MaxScope;

@MaxScope(AccessorScope.PROTOTYPE)
public class ScopePrototypeAccessor extends AccountBaseAccessor {
  public ScopePrototypeAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }
}
