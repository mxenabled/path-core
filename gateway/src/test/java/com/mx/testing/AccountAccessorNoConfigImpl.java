package com.mx.testing;

import lombok.Setter;

import com.mx.testing.accessors.AccountBaseAccessor;

public class AccountAccessorNoConfigImpl extends AccountBaseAccessor {
  @Setter
  TransactionAccessorNoConfigImpl transactions;

  public AccountAccessorNoConfigImpl() {
    transactions = new TransactionAccessorNoConfigImpl();
  }

  public TransactionAccessorNoConfigImpl transactions() {
    return transactions;
  }
}
