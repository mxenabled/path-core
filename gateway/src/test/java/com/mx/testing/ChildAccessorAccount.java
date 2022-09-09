package com.mx.testing;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorResponse;
import com.mx.models.MdxList;
import com.mx.testing.accessors.AccountBaseAccessor;
import com.mx.testing.model.Account;

public class ChildAccessorAccount extends AccountBaseAccessor {
  public ChildAccessorAccount(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public AccessorResponse<MdxList<Account>> list() {
    return super.list();
  }
}
