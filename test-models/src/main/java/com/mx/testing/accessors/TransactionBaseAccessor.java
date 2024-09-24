package com.mx.testing.accessors;

import com.mx.path.core.common.accessor.API;
import com.mx.path.core.common.accessor.AccessorMethodNotImplementedException;
import com.mx.path.core.common.gateway.GatewayAPI;
import com.mx.path.core.common.gateway.GatewayClass;
import com.mx.path.core.common.model.ModelList;
import com.mx.path.gateway.accessor.Accessor;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.testing.model.Transaction;

/**
 * Test class for transaction accessor.
 */
@API(description = "Test transaction accessor")
@GatewayClass
public class TransactionBaseAccessor extends Accessor {

  /**
   * Default constructor.
   */
  public TransactionBaseAccessor() {
  }

  /**
   * Retrieve list of transactions for specified account.
   * @param accountId id of account from which to search for transactions
   * @return list of transactions
   */
  @GatewayAPI
  @API(description = "List transactions in account")
  public AccessorResponse<ModelList<Transaction>> list(String accountId) {
    throw new AccessorMethodNotImplementedException();
  }
}
