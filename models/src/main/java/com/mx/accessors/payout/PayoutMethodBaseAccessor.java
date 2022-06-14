package com.mx.accessors.payout;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.payout.PayoutMethod;

/**
 * Accessor for payout method operations
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/payout/#payout-methods")
public abstract class PayoutMethodBaseAccessor extends Accessor {

  public PayoutMethodBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Create payout method
   * @param recipientId
   * @param payoutMethod
   * @return
   */
  @GatewayAPI
  @API(description = "Create payout method")
  public AccessorResponse<PayoutMethod> create(String recipientId, PayoutMethod payoutMethod) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all payout methods
   * @param recipientId
   * @return
   */
  @GatewayAPI
  @API(description = "List all payout methods")
  public AccessorResponse<MdxList<PayoutMethod>> list(String recipientId) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a payout method
   * @param recipientId
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a payout method")
  public AccessorResponse<PayoutMethod> get(String recipientId, String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update a payout method
   * @param recipientId
   * @param id
   * @param payoutMethod
   * @return
   */
  @GatewayAPI
  @API(description = "Update a payout method")
  public AccessorResponse<PayoutMethod> update(String recipientId, String id, PayoutMethod payoutMethod) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete a payout method
   * @param recipientId
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Delete payout method")
  public AccessorResponse<Void> delete(String recipientId, String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
