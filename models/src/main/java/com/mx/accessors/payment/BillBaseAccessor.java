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
import com.mx.models.payment.Bill;

/**
 * Accessor base for bill operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/payment/#bills">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/payment/#bills")
public abstract class BillBaseAccessor extends Accessor {

  public BillBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get a bill
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a bill")
  public AccessorResponse<Bill> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all bills
   * @return
   */
  @GatewayAPI
  @API(description = "List all bills")
  public AccessorResponse<MdxList<Bill>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
