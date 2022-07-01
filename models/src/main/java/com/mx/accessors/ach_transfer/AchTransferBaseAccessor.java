package com.mx.accessors.ach_transfer;

import lombok.AccessLevel;
import lombok.Getter;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.accessors.ach_transfer.ach_scheduled_transfer.AchScheduledTransferBaseAccessor;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.ach_transfer.AchTransfer;
import com.mx.models.ach_transfer.options.AchTransferListOptions;

/**
 * Accessor base for ACH transfer operations
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/ach_transfer/#transfers")
public abstract class AchTransferBaseAccessor extends Accessor {
  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private AccountBaseAccessor accounts;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private AchAccountBaseAccessor achAccounts;

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private AchScheduledTransferBaseAccessor scheduled;

  public AchTransferBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Account accessor
   * @return AccountBaseAccessor
   */
  @API(description = "Access held accounts")
  public AccountBaseAccessor accounts() {
    if (accounts != null) {
      return accounts;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * ACH account accessor
   * @return AchAccountBaseAccessor
   */
  @API(description = "Access ACH accounts")
  public AchAccountBaseAccessor achAccounts() {
    if (achAccounts != null) {
      return achAccounts;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set account accessor
   * @param accounts
   */
  public void setAccounts(AccountBaseAccessor accounts) {
    this.accounts = accounts;
  }

  /**
   * Set account accessor
   * @param achAccounts
   */
  public void setAchAccounts(AchAccountBaseAccessor achAccounts) {
    this.achAccounts = achAccounts;
  }

  /**
   * Cancels an ACH transfer
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Cancels an ACH transfer")
  public AccessorResponse<Void> cancel(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Creates an ACH transfer
   * @param achTransfer
   * @return AchTransfer
   */
  @GatewayAPI
  @API(description = "Creates an ACH transfer")
  public AccessorResponse<AchTransfer> create(AchTransfer achTransfer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Gets an ACH transfer
   * @param id
   * @return AchTransfer
   */
  @GatewayAPI
  @API(description = "Gets an ACH transfer")
  public AccessorResponse<AchTransfer> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all ACH transfers
   * @return MdxList\<AchTransfer\>
   */
  @GatewayAPI
  @API(description = "List all ACH transfers")
  public AccessorResponse<MdxList<AchTransfer>> list(AchTransferListOptions options) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Updates an ACH transfer
   * @param id
   * @param achTransfer
   * @return AchTransfer
   */
  @GatewayAPI
  @API(description = "Updates an ACH transfer")
  public AccessorResponse<AchTransfer> update(String id, AchTransfer achTransfer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Accessor for scheduled ACH transfer operations
   * @return accessor
   */
  @API(specificationUrl = "https://developer.mx.com/drafts/mdx/ach_transfer/#ach-scheduled-transfers")
  public AchScheduledTransferBaseAccessor scheduled() {
    if (scheduled != null) {
      return scheduled;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set ACH scheduled transfer accessor
   * @param scheduled
   */
  public void setScheduled(AchScheduledTransferBaseAccessor scheduled) {
    this.scheduled = scheduled;
  }
}
