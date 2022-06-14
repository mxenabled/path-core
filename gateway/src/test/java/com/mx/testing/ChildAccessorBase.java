package com.mx.testing;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.BaseAccessor;
import com.mx.accessors.account.AccountBaseAccessor;
import com.mx.path.gateway.configuration.annotations.ChildAccessor;

@ChildAccessor(ChildAccessorAccount.class)
public class ChildAccessorBase extends BaseAccessor {
  public ChildAccessorBase(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public AccountBaseAccessor accounts() {
    return super.accounts();
  }
}
