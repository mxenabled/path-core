package com.mx.testing.accessors;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.testing.model.Transaction;

@API(description = "Test transaction accessor")
@GatewayClass
public class TransactionBaseAccessor extends Accessor {
  public TransactionBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get all accounts
   * @return
   */
  @GatewayAPI
  @API(description = "List transactions in account")
  public AccessorResponse<MdxList<Transaction>> list(String accountId) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
