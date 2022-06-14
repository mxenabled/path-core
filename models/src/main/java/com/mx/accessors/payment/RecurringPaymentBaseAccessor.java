package com.mx.accessors.payment;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.Frequency;
import com.mx.models.MdxList;
import com.mx.models.payment.RecurringPayment;

/**
 * Accessor base for recurring payment operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/payment#recurring-payments">specifications</a>
 */

@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/payment#recurring-payments")
public abstract class RecurringPaymentBaseAccessor extends Accessor {

  public RecurringPaymentBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Create a recurring payment
   * @param recurringPayment
   * @return
   */
  @GatewayAPI
  @API(description = "Create a recurring payment")
  public AccessorResponse<RecurringPayment> create(RecurringPayment recurringPayment) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a recurring payment
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a recurring payment")
  public AccessorResponse<RecurringPayment> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List frequency options for recurring payments
   * @return
   */
  @GatewayAPI
  @API(description = "List frequency options for recurring payments")
  public AccessorResponse<MdxList<Frequency>> frequencies() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all recurring payments
   * @return
   */
  @GatewayAPI
  @API(description = "List all recurring payments")
  public AccessorResponse<MdxList<RecurringPayment>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Cancel a recurring payment
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Cancel a recurring payment")
  public AccessorResponse<Void> cancel(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Update recurring payment
   * @param id
   * @param recurringPayment
   * @return
   */
  @GatewayAPI
  @API(description = "Update recurring payment")
  public AccessorResponse<RecurringPayment> update(String id, RecurringPayment recurringPayment) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
