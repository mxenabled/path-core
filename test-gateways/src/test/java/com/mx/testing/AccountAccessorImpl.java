package com.mx.testing;

import lombok.Setter;

import com.mx.path.core.common.accessor.AccessorConfiguration;
import com.mx.path.core.common.accessor.AccessorResponse;
import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.testing.accessors.AccountBaseAccessor;
import com.mx.testing.accessors.TransactionBaseAccessor;
import com.mx.testing.model.Account;

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

  @Override
  public AccessorResponse<Account> get(String id) {
    Account account = new Account();
    return AccessorResponse.<Account>builder().result(account).status(PathResponseStatus.OK).build();
  }
}
