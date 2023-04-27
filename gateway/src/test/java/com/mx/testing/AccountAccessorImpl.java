package com.mx.testing;

import lombok.Setter;

import com.mx.path.core.common.accessor.AccessorConfiguration;
import com.mx.testing.accessors.AccountBaseAccessor;

public class AccountAccessorImpl extends AccountBaseAccessor {
  @Setter
  TransactionAccessorImpl transactions;

  public AccountAccessorImpl(AccessorConfiguration configuration) {
    super(configuration);
    transactions = new TransactionAccessorImpl(configuration);
  }

  public TransactionAccessorImpl transactions() {
    return transactions;
  }
}
