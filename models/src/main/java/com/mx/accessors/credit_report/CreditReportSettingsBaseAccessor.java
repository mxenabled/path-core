package com.mx.accessors.credit_report;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.credit_report.CreditReportSettings;

/**
 * Accessor base for credit report settings operations
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/credit_report/#mdx-credit-reports")
public abstract class CreditReportSettingsBaseAccessor extends Accessor {

  public CreditReportSettingsBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get credit report settings
   * @return
   */
  @GatewayAPI
  @API(description = "Get credit report settings")
  public AccessorResponse<CreditReportSettings> get() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
