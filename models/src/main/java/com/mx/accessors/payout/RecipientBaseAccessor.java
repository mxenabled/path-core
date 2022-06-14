package com.mx.accessors.payout;

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
import com.mx.models.payout.Recipient;

/**
 * Accessor for recipient operations
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/payout/#recipients")
public abstract class RecipientBaseAccessor extends Accessor {

  @GatewayAPI
  @Getter(AccessLevel.PROTECTED)
  private PayoutMethodBaseAccessor payoutMethods;

  public RecipientBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Create a recipient
   * @param recipient
   * @return
   */
  @GatewayAPI
  @API(description = "Create a recipient")
  public AccessorResponse<Recipient> create(Recipient recipient) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all recipients
   * @param includePayoutMethods
   * @return
   */
  @GatewayAPI
  @API(description = "List all recipients")
  public AccessorResponse<MdxList<Recipient>> list(boolean includePayoutMethods) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a recipient
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a recipient")
  public AccessorResponse<Recipient> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update a recipient
   * @param id
   * @param recipient
   * @return
   */
  @GatewayAPI
  @API(description = "Update a recipient")
  public AccessorResponse<Recipient> update(String id, Recipient recipient) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete a recipient
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Delete a recipient")
  public AccessorResponse<Void> delete(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Accessor for payout method operations
   * @return accessor
   */
  @API
  public PayoutMethodBaseAccessor payoutMethods() {
    if (payoutMethods != null) {
      return payoutMethods;
    }

    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Set payout method accessor
   * @param payoutMethods
   */
  public void setPayoutMethods(PayoutMethodBaseAccessor payoutMethods) {
    this.payoutMethods = payoutMethods;
  }
}
