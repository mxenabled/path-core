package com.mx.testing;

import lombok.Setter;

import com.mx.testing.accessors.AccountBaseAccessor;
import com.mx.testing.accessors.BaseAccessor;
import com.mx.testing.accessors.IdBaseAccessor;

public class BaseAccessorImpl extends BaseAccessor {

  @Setter
  AccountBaseAccessor accounts;

  @Setter
  IdBaseAccessor id;

  public BaseAccessorImpl() {
    id = new IdAccessorImpl();
    accounts = new AccountAccessorImpl();
  }

  @Override
  public AccountBaseAccessor accounts() {
    return accounts;
  }

  @Override
  public IdBaseAccessor id() {
    return id;
  }

}
