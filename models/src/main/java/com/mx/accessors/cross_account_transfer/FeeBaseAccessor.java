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
import com.mx.models.cross_account_transfer.options.FeeListOptions;
import com.mx.models.transfer.Fee;

/**
 * Accessor base for cross account transfer fee operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/cross_account_transfer/#fees-list-cross-account-transfer-fees">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/cross_account_transfer/#fees-list-cross-account-transfer-fees")
public abstract class FeeBaseAccessor extends Accessor {

  public FeeBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * List fees for cross account transfers
   * @return
   */
  @GatewayAPI
  @API(description = "List all fees for cross account transfers")
  public AccessorResponse<MdxList<Fee>> list(FeeListOptions options) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
