package com.mx.testing;

import lombok.Setter;

import com.mx.accessors.AccessorConfiguration;
import com.mx.testing.accessors.AccountBaseAccessor;
import com.mx.testing.accessors.TransactionBaseAccessor;

public class AccountAccessorImpl extends AccountBaseAccessor {
  @Setter
  TransactionBaseAccessor transactions;

  public AccountAccessorImpl(AccessorConfiguration configuration) {
    super(configuration);
    transactions = new TransactionAccessorImpl(configuration);
  }

  @Override
  public TransactionBaseAccessor transactions() {
    return transactions;
  }
}
