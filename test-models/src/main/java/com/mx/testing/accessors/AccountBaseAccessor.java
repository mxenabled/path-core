package com.mx.testing.accessors;

import lombok.Getter;
import lombok.Setter;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
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

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get all accounts
   * @return
   */
  @GatewayAPI
  @API(description = "Get all user's account")
  public AccessorResponse<MdxList<Account>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get all accounts
   * @return
   */
  @GatewayAPI
  @API(description = "Get user's account by account")
  public AccessorResponse<Account> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
