package com.mx.testing;

import lombok.Setter;

import com.mx.testing.accessors.AccountBaseAccessor;

public class AccountAccessorImpl extends AccountBaseAccessor {
  @Setter
  TransactionAccessorImpl transactions;

  public AccountAccessorImpl() {
    transactions = new TransactionAccessorImpl();
  }

  public TransactionAccessorImpl transactions() {
    return transactions;
  }
}
