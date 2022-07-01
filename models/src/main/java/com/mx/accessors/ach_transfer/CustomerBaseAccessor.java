package com.mx.accessors.ach_transfer;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.ach_transfer.Customer;

/**
 * Accessor base for Customer
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/ach_transfer/#customer">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/ach_transfer/#customer")
@Deprecated // This is going to be removed in favor of the new ACH transfer spec: https://developer.mx.com/drafts/mdx/ach_transfer/#mdx-ach-transfer
public abstract class CustomerBaseAccessor extends Accessor {
  public CustomerBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get a customer
   * @return
   */
  @GatewayAPI
  @API(description = "Get a customer")
  public AccessorResponse<Customer> get() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Create a customer
   * @param customer
   * @return
   */
  @GatewayAPI
  @API(description = "Create a customer")
  public AccessorResponse<Customer> create(Customer customer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update a customer
   * @param customer
   * @return
   */
  @GatewayAPI
  @API(description = "Update a customer")
  public AccessorResponse<Customer> update(Customer customer) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
