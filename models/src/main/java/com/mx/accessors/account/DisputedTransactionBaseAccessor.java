package com.mx.accessors.account;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.dispute.DisputedTransaction;

/**
 * Accessor for disputed transaction operations
 */
@GatewayClass
@API(description = "Access to user disputed transactions", specificationUrl = "https://developer.mx.com/drafts/mdx/accounts/#disputes-disputed-transaction")
public abstract class DisputedTransactionBaseAccessor extends Accessor {

  public DisputedTransactionBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get all disputed transactions
   * @param disputeId
   * @return
   */
  @GatewayAPI
  @API(description = "Get all user's disputed transaction")
  public AccessorResponse<MdxList<DisputedTransaction>> list(String disputeId) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Create a disputed transaction
   * @param disputeId
   * @param disputedTransaction
   * @return
   */
  @GatewayAPI
  @API(description = "Create disputed transaction")
  public AccessorResponse<DisputedTransaction> create(String disputeId, DisputedTransaction disputedTransaction) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete a disputed transaction
   * @param disputeId
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Delete disputed transaction")
  public AccessorResponse<Void> delete(String disputeId, String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a disputed transaction by id
   * @param disputeId
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a disputed transaction by id")
  public AccessorResponse<DisputedTransaction> get(String disputeId, String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update disputed transaction
   * @param disputeId
   * @param id
   * @param disputedTransaction
   * @return
   */
  @GatewayAPI
  @API(description = "Update given account")
  public AccessorResponse<DisputedTransaction> update(String disputeId, String id, DisputedTransaction disputedTransaction) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
