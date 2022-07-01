package com.mx.accessors.ach_transfer;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.ach_transfer.FundingSource;

/**
 * Accessor base for Funding Sources
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/ach_transfer/#funding-sources">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/ach_transfer/#funding-sources")
@Deprecated // This is being replaced by: https://developer.mx.com/drafts/mdx/ach_transfer/#mdx-ach-transfer
public abstract class FundingSourceBaseAccessor extends Accessor {

  public FundingSourceBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * List all funding sources
   * @return
   */
  @GatewayAPI
  @API(description = "List all funding sources")
  public AccessorResponse<MdxList<FundingSource>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a funding source
   * @param fundingSourceId
   * @return
   */
  @GatewayAPI
  @API(description = "Get a funding source")
  public AccessorResponse<FundingSource> get(String fundingSourceId) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Create a funding source
   * @param fundingSource
   * @return
   */
  @GatewayAPI
  @API(description = "Create a funding source")
  public AccessorResponse<FundingSource> create(FundingSource fundingSource) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update a funding source
   * @param fundingSourceId
   * @param fundingSource
   * @return
   */
  @GatewayAPI
  @API(description = "Update a funding source")
  public AccessorResponse<FundingSource> update(String fundingSourceId, FundingSource fundingSource) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete a funding source
   * @param fundingSourceId
   * @return
   */
  @GatewayAPI
  @API(description = "Delete a funding source")
  public AccessorResponse<Void> delete(String fundingSourceId) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
