package com.mx.testing.accessors;

import com.mx.path.core.common.accessor.API;
import com.mx.path.core.common.accessor.AccessorMethodNotImplementedException;
import com.mx.path.core.common.gateway.GatewayAPI;
import com.mx.path.core.common.gateway.GatewayClass;
import com.mx.path.core.common.model.ModelList;
import com.mx.path.gateway.accessor.Accessor;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
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
  public AccessorResponse<ModelList<Transaction>> list(String accountId) {
    throw new AccessorMethodNotImplementedException();
  }
}
