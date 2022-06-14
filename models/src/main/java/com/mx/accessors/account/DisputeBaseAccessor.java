package com.mx.accessors.account;

import lombok.AccessLevel;
import lombok.Getter;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.dispute.Dispute;

/**
 * Accessor for account operations
 */
@GatewayClass
@API(description = "Access to user disputes", specificationUrl = "https://developer.mx.com/drafts/mdx/accounts/#disputes")
public abstract class DisputeBaseAccessor extends Accessor {

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private DisputedTransactionBaseAccessor disputedTransactions;

  public DisputeBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get a dispute by id
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a dispute by id")
  public AccessorResponse<Dispute> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get all disputes
   * @return
   */
  @GatewayAPI
  @API(description = "Get all user's disputes")
  public AccessorResponse<MdxList<Dispute>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Start a dispute
   * @param dispute
   * @return
   */
  @GatewayAPI
  @API(description = "Start dispute")
  public AccessorResponse<Dispute> start(Dispute dispute) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update dispute
   * @param dispute
   * @return
   */
  @GatewayAPI
  @API(description = "Update given dispute")
  public AccessorResponse<Dispute> update(String id, Dispute dispute) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Submit dispute
   * @return
   */
  @GatewayAPI
  @API(description = "Submit dispute")
  public AccessorResponse<Dispute> submit(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Cancel a dispute
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Cancel dispute")
  public AccessorResponse<Void> cancel(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * DisputedTransaction accessor
   * @return
   */
  @API(description = "Access account's disputed transactions")
  public DisputedTransactionBaseAccessor disputedTransactions() {
    if (disputedTransactions != null) {
      return disputedTransactions;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set disputed transaction accessor
   * @param disputedTransactions
   */
  public void setDisputedTransactions(DisputedTransactionBaseAccessor disputedTransactions) {
    this.disputedTransactions = disputedTransactions;
  }
}
