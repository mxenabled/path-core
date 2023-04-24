package com.mx.testing;

import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.models.ModelList;
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
