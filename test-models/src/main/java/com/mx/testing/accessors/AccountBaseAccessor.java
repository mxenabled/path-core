package com.mx.testing.accessors;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.accessor.API;
import com.mx.path.core.common.accessor.Accessor;
import com.mx.path.core.common.accessor.AccessorConfiguration;
import com.mx.path.core.common.accessor.AccessorMethodNotImplementedException;
import com.mx.path.core.common.accessor.AccessorResponse;
import com.mx.path.core.common.gateway.GatewayAPI;
import com.mx.path.core.common.gateway.GatewayClass;
import com.mx.path.core.common.model.ModelList;
import com.mx.testing.model.Account;

@API(description = "Test account accessor")
@GatewayClass
public class AccountBaseAccessor extends Accessor {
  @GatewayAPI
  @Getter
  @Setter
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
  public AccessorResponse<ModelList<Account>> list() {
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
