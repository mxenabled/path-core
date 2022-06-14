package com.mx.accessors.payment;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.payment.Payee;

/**
 * Accessor base for payee operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/payment/#payees">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/payment/#payees")
public abstract class PayeeBaseAccessor extends Accessor {

  public PayeeBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Create a payee
   * @param payee
   * @return
   */
  @GatewayAPI
  @API(description = "Create a payee")
  public AccessorResponse<Payee> create(Payee payee) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Delete a payee
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Delete a payee")
  public AccessorResponse<Void> delete(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a payee
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a payee")
  public AccessorResponse<Payee> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all payee
   * @return
   */
  @GatewayAPI
  @API(description = "List all payee")
  public AccessorResponse<MdxList<Payee>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update a payee
   * @param payee
   * @return
   */
  @GatewayAPI
  @API(description = "Update a payee")
  public AccessorResponse<Payee> update(String id, Payee payee) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
