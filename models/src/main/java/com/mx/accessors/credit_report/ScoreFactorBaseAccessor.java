package com.mx.accessors.credit_report;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.credit_report.CreditReportScoreFactor;

/**
 * Accessor base for credit report operations
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/credit_report/#credit-report-score-factors")
public abstract class ScoreFactorBaseAccessor extends Accessor {

  public ScoreFactorBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get a score factor
   * @param reportId
   * @param factorId
   * @return
   */
  @GatewayAPI
  @API(description = "Get a score factor")
  public AccessorResponse<CreditReportScoreFactor> get(String reportId, String factorId) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List score factors
   * @param reportId
   * @return
   */
  @GatewayAPI
  @API(description = "List score factors")
  public AccessorResponse<MdxList<CreditReportScoreFactor>> list(String reportId) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
