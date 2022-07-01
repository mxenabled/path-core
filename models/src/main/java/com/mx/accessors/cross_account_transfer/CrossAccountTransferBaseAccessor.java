package com.mx.accessors.cross_account_transfer;

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
import com.mx.models.AccountType;
import com.mx.models.MdxList;
import com.mx.models.account.Account;
import com.mx.models.cross_account_transfer.CrossAccountTransfer;

/**
 * Accessor base for cross-account transfer operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/cross_account_transfer/#mdx-cross-account-transfer">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/cross_account_transfer/#mdx-cross-account-transfer")
public abstract class CrossAccountTransferBaseAccessor extends Accessor {

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private CrossAccountRecurringTransferBaseAccessor crossAccountRecurring;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private DestinationBaseAccessor destination;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private FeeBaseAccessor fees;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private FrequencyBaseAccessor frequency;

  public CrossAccountTransferBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * List source account options for cross-account transfer
   * @return
   */
  @GatewayAPI
  @API(description = "List source account options for cross-account transfer")
  public AccessorResponse<MdxList<Account>> accounts() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List account type options for destination account
   * @return
   */
  @GatewayAPI
  @API(description = "List account type options for destination account")
  public AccessorResponse<MdxList<AccountType>> accountTypes() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Create a cross-account transfer
   * @param crossAccountTransfer
   * @return
   */
  @GatewayAPI
  @API(description = "Create a cross-account transfer")
  public AccessorResponse<CrossAccountTransfer> create(CrossAccountTransfer crossAccountTransfer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Accessor for recurring cross account transfer operations
   * @return accessor
   */
  @API
  public CrossAccountRecurringTransferBaseAccessor crossAccountRecurring() {
    return crossAccountRecurring;
  }

  /**
   * Delete a scheduled cross account transfer
   * @param id the id for the transfer
   * @return a AccessorResponse object with Void content
   */
  @GatewayAPI
  @API(description = "delete a scheduled cross-account transfer")
  public AccessorResponse<Void> delete(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Accessor for destination accounts operations
   * @return accessor
   */
  @API
  public DestinationBaseAccessor destination() {
    return destination;
  }

  /**
   * Accessor for cross account transfer fees operations
   */
  @API(description = "Access cross account transfer fees")
  public FeeBaseAccessor fees() {
    if (fees != null) {
      return fees;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Accessor for frequency operations
   */
  @API
  public FrequencyBaseAccessor frequency() {
    return frequency;
  }

  /**
   * Get a cross-account transfer
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a cross-account transfer")
  public AccessorResponse<CrossAccountTransfer> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all cross-account transfers
   * @return list of cross account transfers
   */
  @GatewayAPI
  @API(description = "List all cross-account transfers")
  public AccessorResponse<MdxList<CrossAccountTransfer>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update a scheduled cross account transfer
   * @param id the transfer id
   * @param crossAccountTransfer transfer object that contains data for update
   * @return updated transfer
   */
  @GatewayAPI
  @API(description = "Update a cross-account transfer")
  public AccessorResponse<CrossAccountTransfer> update(String id, CrossAccountTransfer crossAccountTransfer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set cross account recurring transfer accessor
   * @param crossAccountRecurring
   */
  public void setCrossAccountRecurring(CrossAccountRecurringTransferBaseAccessor crossAccountRecurring) {
    this.crossAccountRecurring = crossAccountRecurring;
  }

  /**
   * Set destination account accessor
   * @param destination
   */
  public void setDestination(DestinationBaseAccessor destination) {
    this.destination = destination;
  }

  /**
   * Set fees accessor
   * @param fees
   */
  public void setFees(FeeBaseAccessor fees) {
    this.fees = fees;
  }

  /**
   * Set frequency accessor
   * @param frequency
   */
  public void setFrequency(FrequencyBaseAccessor frequency) {
    this.frequency = frequency;
  }
}
