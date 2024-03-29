package com.mx.testing;

import com.mx.path.core.common.model.ModelList;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.testing.accessors.AccountBaseAccessor;
import com.mx.testing.model.Account;

public class ChildAccessorAccount extends AccountBaseAccessor {
  public ChildAccessorAccount(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public AccessorResponse<ModelList<Account>> list() {
    return super.list();
  }
}
