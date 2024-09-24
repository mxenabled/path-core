package com.mx.testing.accessors;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.accessor.API;
import com.mx.path.core.common.accessor.AccessorMethodNotImplementedException;
import com.mx.path.core.common.gateway.GatewayAPI;
import com.mx.path.core.common.gateway.GatewayClass;
import com.mx.path.core.common.model.ModelList;
import com.mx.path.core.common.remote.RemoteOperation;
import com.mx.path.gateway.accessor.Accessor;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.testing.model.Account;

/**
 * Test class for account accessor.
 */
@API(description = "Test account accessor")
@GatewayClass
public class AccountBaseAccessor extends Accessor {

  /**
   * -- GETTER --
   * Return transaction accessor.
   *
   * @return transaction accessor
   *
   * -- SETTER --
   * Set transaction accessor.
   *
   * @param transactions transactions to set
   */
  @GatewayAPI
  @Getter
  @Setter
  private TransactionBaseAccessor transactions;

  /**
   * Default constructor.
   */
  public AccountBaseAccessor() {
  }

  /**
   * Accessor for account operations.
   * @return accessor
   */
  @API
  public TransactionBaseAccessor transactions() {
    if (transactions != null) {
      return transactions;
    }

    throw new AccessorMethodNotImplementedException();
  }

  /**
   * Get all accounts.
   * @return list of accounts
   */
  @GatewayAPI
  @API(description = "Get all user's account")
  public AccessorResponse<ModelList<Account>> list() {
    throw new AccessorMethodNotImplementedException();
  }

  /**
   * Get all accounts.
   * @param includeDeleted include deleted accounts on response
   * @return list of accounts
   */
  @GatewayAPI
  @API(description = "Get all user's account", version = "20240101")
  @RemoteOperation("list20240101")
  public AccessorResponse<ModelList<com.mx.testing.models.v20240101.Account>> list(boolean includeDeleted) {
    throw new AccessorMethodNotImplementedException();
  }

  /**
   * Get all accounts by id.
   * @param id account id
   * @return account
   */
  @GatewayAPI
  @API(description = "Get user's account by account")
  public AccessorResponse<Account> get(String id) {
    throw new AccessorMethodNotImplementedException();
  }

  /**
   * Get account by id.
   * @param id account id
   * @return account
   */
  @GatewayAPI
  @API(description = "Get user's account by account", version = "20240101")
  public AccessorResponse<com.mx.testing.models.v20240101.Account> get20240101(String id) {
    throw new AccessorMethodNotImplementedException();
  }

  /**
   * Create account.
   * @param account account to create
   * @return account created
   */
  @GatewayAPI
  @API(description = "Create account")
  public AccessorResponse<Account> create(Account account) {
    throw new AccessorMethodNotImplementedException();
  }

  /**
   * Create account.
   * @param account account to create
   * @return account created
   */
  @GatewayAPI
  @API(description = "Create account", version = "20240101")
  @RemoteOperation("create20240101")
  public AccessorResponse<com.mx.testing.models.v20240101.Account> create(com.mx.testing.models.v20240101.Account account) {
    throw new AccessorMethodNotImplementedException();
  }
}
