package com.mx.accessors.transfer;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.common.remote.RemoteOperation;
import com.mx.models.MdxList;
import com.mx.models.transfer.Fee;
import com.mx.models.transfer.options.FeeListOptions;

/**
 * Accessor base for transfer fee operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/transfer/#transfers-transfer-fees">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/transfer/#transfers-transfer-fees")
public abstract class FeeBaseAccessor extends Accessor {

  public FeeBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Return a transfer's fees
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a transfer's fees")
  @RemoteOperation("listById")
  public AccessorResponse<MdxList<Fee>> list(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Return transfer fees based on options
   * @param options
   * @return
   */
  @GatewayAPI
  @API(description = "Get transfer fees based on options")
  public AccessorResponse<MdxList<Fee>> list(FeeListOptions options) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
