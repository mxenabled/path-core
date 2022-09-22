package com.mx.testing.accessors;

import lombok.Getter;

import com.mx.common.accessors.API;
import com.mx.common.accessors.Accessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.exception.request.accessor.AccessorMethodNotImplementedException;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.common.models.MdxList;
import com.mx.testing.model.Account;

@API(description = "Test account accessor")
@GatewayClass
public class AccountBaseAccessor extends Accessor {
  @GatewayAPI
  @Getter
  private TransactionBaseAccessor transactions;

  public AccountBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Accessor for account operations
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
   * Get all accounts
   * @return
   */
  @GatewayAPI
  @API(description = "Get all user's account")
  public AccessorResponse<MdxList<Account>> list() {
    throw new AccessorMethodNotImplementedException();
  }

  /**
   * Get all accounts
   * @return
   */
  @GatewayAPI
  @API(description = "Get user's account by account")
  public AccessorResponse<Account> get(String id) {
    throw new AccessorMethodNotImplementedException();
  }
}
