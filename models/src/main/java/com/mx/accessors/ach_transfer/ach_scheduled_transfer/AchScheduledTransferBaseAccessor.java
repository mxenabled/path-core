package com.mx.accessors.ach_transfer.ach_scheduled_transfer;

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
import com.mx.models.ach_transfer.AchScheduledTransfer;
import com.mx.models.ach_transfer.options.AchScheduledTransferListOptions;

/**
 * Accessor base for ACH scheduled transfer operations
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/ach_transfer/#ach-scheduled-transfers")
public abstract class AchScheduledTransferBaseAccessor extends Accessor {

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private FrequencyBaseAccessor frequencies;

  public AchScheduledTransferBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Accessor for ACH scheduled transfer frequencies
   * @return FrequencyBaseAccessor
   */
  @API(description = "List scheduled ACH transfer frequencies")
  public FrequencyBaseAccessor frequencies() {
    if (frequencies != null) {
      return frequencies;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set accessor for ACH scheduled transfer frequencies
   * @param frequencies
   */
  public void setFrequencies(FrequencyBaseAccessor frequencies) {
    this.frequencies = frequencies;
  }

  /**
   * Cancels an ACH scheduled transfer
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Cancels an ACH scheduled transfer")
  public AccessorResponse<Void> cancel(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Creates an ACH scheduled transfer
   * @param achScheduledTransfer
   * @return AchScheduledTransfer
   */
  @GatewayAPI
  @API(description = "Creates an ACH scheduled transfer")
  public AccessorResponse<AchScheduledTransfer> create(AchScheduledTransfer achScheduledTransfer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Gets an ACH scheduled transfer
   * @param id
   * @return AchScheduledTransfer
   */
  @GatewayAPI
  @API(description = "Gets an ACH scheduled transfer")
  public AccessorResponse<AchScheduledTransfer> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all ACH scheduled transfers
   * @return MdxList\<AchScheduledTransfer\>
   */
  @GatewayAPI
  @API(description = "List all ACH scheduled transfers")
  public AccessorResponse<MdxList<AchScheduledTransfer>> list(AchScheduledTransferListOptions options) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Updates an ACH scheduled transfer
   * @param id
   * @param achScheduledTransfer
   * @return AchScheduledTransfer
   */
  @GatewayAPI
  @API(description = "Updates an ACH scheduled transfer")
  public AccessorResponse<AchScheduledTransfer> update(String id, AchScheduledTransfer achScheduledTransfer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
