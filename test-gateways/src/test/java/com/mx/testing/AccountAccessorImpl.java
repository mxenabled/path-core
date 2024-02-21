package com.mx.testing;

import lombok.Setter;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
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

  @Override
  public AccessorResponse<com.mx.testing.models.v20240101.Account> get20240101(String id) {
    com.mx.testing.models.v20240101.Account account = new com.mx.testing.models.v20240101.Account();
    return AccessorResponse.<com.mx.testing.models.v20240101.Account>builder().result(account).status(PathResponseStatus.OK).build();
  }

  @Override
  public AccessorResponse<Account> create(Account account) {
    return AccessorResponse.<Account>builder().result(account).status(PathResponseStatus.OK).build();
  }

  @Override
  public AccessorResponse<com.mx.testing.models.v20240101.Account> create(com.mx.testing.models.v20240101.Account account) {
    return AccessorResponse.<com.mx.testing.models.v20240101.Account>builder().result(account).status(PathResponseStatus.OK).build();
  }
}
