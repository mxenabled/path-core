package com.mx.accessors.cross_account_transfer;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.cross_account_transfer.CrossAccountRecurringTransfer;

/**
 * Accessor base for recurring cross account transfer operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/cross_account_transfer/#recurring-cross-account-transfers">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/cross_account_transfer/#recurring-cross-account-transfers")
public class CrossAccountRecurringTransferBaseAccessor extends Accessor {
  public CrossAccountRecurringTransferBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Create a recurring cross account transfer
   * @param crossAccountRecurringTransfer
   * @return
   */
  @GatewayAPI
  @API(description = "Create a recurring cross account transfer")
  public AccessorResponse<CrossAccountRecurringTransfer> create(CrossAccountRecurringTransfer crossAccountRecurringTransfer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete a recurring cross account transfer
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "delete the recurring cross account transfer")
  public AccessorResponse<Void> delete(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a recurring cross account transfer
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a recurring cross account transfer")
  public AccessorResponse<CrossAccountRecurringTransfer> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all recurring cross account transfers
   * @return
   */
  @GatewayAPI
  @API(description = "List all recurring cross account transfers")
  public AccessorResponse<MdxList<CrossAccountRecurringTransfer>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Skip next payment of recurring cross account transfer
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Skip next occurrence of recurring cross account transfer")
  public AccessorResponse<CrossAccountRecurringTransfer> skipNext(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update recurring cross account transfer
   * @param id
   * @param crossAccountRecurringTransfer
   * @return
   */
  @GatewayAPI
  @API(description = "Update recurring cross account transfer")
  public AccessorResponse<CrossAccountRecurringTransfer> update(String id, CrossAccountRecurringTransfer crossAccountRecurringTransfer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
