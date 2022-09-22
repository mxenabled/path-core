package com.mx.testing.accessors;

import com.mx.common.accessors.API;
import com.mx.common.accessors.Accessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.exception.request.accessor.AccessorMethodNotImplementedException;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.common.models.MdxList;
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
    throw new AccessorMethodNotImplementedException();
  }
}
