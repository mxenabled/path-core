package com.mx.accessors.ach_transfer.ach_scheduled_transfer;

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
import com.mx.models.ach_transfer.options.FrequencyListOptions;

/**
 * Accessor for ACH scheduled transfer frequencies
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/ach_transfer/#ach-scheduled-transfers-scheduled-ach-transfer-frequencies")
public abstract class FrequencyBaseAccessor extends Accessor {

  public FrequencyBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * List frequency options for ACH scheduled transfers
   * @return
   */
  @GatewayAPI
  @API(description = "List frequency options for ACH scheduled transfers")
  public AccessorResponse<MdxList<Frequency>> list(FrequencyListOptions options) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
