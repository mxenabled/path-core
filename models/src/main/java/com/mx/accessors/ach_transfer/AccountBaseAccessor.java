package com.mx.accessors.ach_transfer;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.models.MdxList;
import com.mx.models.account.Account;
import com.mx.models.ach_transfer.options.AccountListOptions;

/**
 * Accessor base for ACH held-account operations
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/ach_transfer/#ach-account-list-held-accounts")
public abstract class AccountBaseAccessor extends Accessor {
  public AccountBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Lists held accounts according to search criteria
   * @param options
   * @return MdxList\<Account\>
   */
  @GatewayAPI
  @API(description = "Lists accounts according to search criteria")
  public AccessorResponse<MdxList<Account>> list(AccountListOptions options) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
