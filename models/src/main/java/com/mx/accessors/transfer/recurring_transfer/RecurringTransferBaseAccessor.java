package com.mx.accessors.transfer.recurring_transfer;

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
import com.mx.models.transfer.RecurringTransfer;
import com.mx.models.transfer.options.RecurringTransferListOptions;

/**
 * Accessor base for recurring transfer operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/transfer/#recurring-transfers">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/transfer/#recurring-transfers")
public abstract class RecurringTransferBaseAccessor extends Accessor {

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private FrequencyBaseAccessor frequencies;

  public RecurringTransferBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Create a recurring transfer
   * @param recurringTransfer
   * @return
   */
  @GatewayAPI
  @API(description = "Create a recurring transfer")
  public AccessorResponse<RecurringTransfer> create(RecurringTransfer recurringTransfer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete a recurring transfer
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Delete a recurring transfer")
  public AccessorResponse<Void> delete(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a recurring transfer
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a recurring transfer")
  public AccessorResponse<RecurringTransfer> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List recurring transfers
   * @return
   */
  @GatewayAPI
  @API(description = "List recurring transfers")
  public AccessorResponse<MdxList<RecurringTransfer>> list(RecurringTransferListOptions options) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Skip next payment of recurring transfer
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Skip next occurrence of recurring transfer")
  public AccessorResponse<RecurringTransfer> skipNext(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update a recurring transfer
   * @param id
   * @param recurringTransfer
   * @return
   */
  @GatewayAPI
  @API(description = "Update recurring transfer")
  public AccessorResponse<RecurringTransfer> update(String id, RecurringTransfer recurringTransfer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Accessor for recurring transfer frequency operations
   * @return accessor
   */
  @API(description = "Access recurring transfer frequencies")
  public FrequencyBaseAccessor frequencies() {
    if (frequencies != null) {
      return frequencies;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set recurring transfer frequencies accessor
   * @param frequencies
   */
  public void setFrequencies(FrequencyBaseAccessor frequencies) {
    this.frequencies = frequencies;
  }
}
