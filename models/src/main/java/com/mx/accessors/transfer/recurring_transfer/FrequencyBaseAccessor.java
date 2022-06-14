package com.mx.accessors.transfer.recurring_transfer;

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
import com.mx.models.transfer.options.FrequencyListOptions;

/**
 * Accessor base for recurring transfer frequency operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/transfer/index.html#recurring-transfer-frequencies">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/transfer/index.html#recurring-transfer-frequencies")
public abstract class FrequencyBaseAccessor extends Accessor {

  public FrequencyBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * List frequency options for recurring transfers
   * @param options
   * @return
   */
  @GatewayAPI
  @API(description = "List frequency options for recurring transfers")
  public AccessorResponse<MdxList<Frequency>> list(FrequencyListOptions options) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
