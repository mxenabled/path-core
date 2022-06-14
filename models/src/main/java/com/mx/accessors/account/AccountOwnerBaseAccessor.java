package com.mx.accessors.account;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.account.AccountOwner;

/**
 * Accessor for account owner operations
 */
@GatewayClass
public abstract class AccountOwnerBaseAccessor extends Accessor {
  public AccountOwnerBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Gets the account owner
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Gets the account owner")
  public AccessorResponse<AccountOwner> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
