package com.mx.accessors.account;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.account.AccountNumbers;

/**
 * Accessor for account number operations
 */
@GatewayClass
@API(description = "Access to user account numbers")
public abstract class AccountNumberBaseAccessor extends Accessor {
  public AccountNumberBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get account numbers
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get account numbers")
  public AccessorResponse<AccountNumbers> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
