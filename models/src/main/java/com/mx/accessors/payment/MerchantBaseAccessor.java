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
import com.mx.models.payment.Merchant;
import com.mx.models.payment.MerchantCategory;

/**
 * Accessor base for merchant operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/payment/#merchants">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/payment/#merchants")
public abstract class MerchantBaseAccessor extends Accessor {

  public MerchantBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * List all merchants
   * @param name
   * @return
   */
  @GatewayAPI
  @API(description = "List all merchants")
  public AccessorResponse<MdxList<Merchant>> list(String name) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Search merchants by category
   * @param merchantCategoryId
   * @return
   */
  @GatewayAPI
  @API(description = "List all merchants")
  public AccessorResponse<MdxList<Merchant>> search(String merchantCategoryId) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all merchant categories
   * @return
   */
  @GatewayAPI
  @API(description = "List all merchant categories")
  public AccessorResponse<MdxList<MerchantCategory>> categories() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
