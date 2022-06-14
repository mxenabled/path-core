package com.mx.accessors.transfer;

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
import com.mx.models.transfer.options.AccountListOptions;

/**
 * Accessor base for transfer account operations
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/transfer/index.html#transfer-accounts">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/transfer/index.html#transfer-accounts")
public abstract class AccountBaseAccessor extends Accessor {

  public AccountBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * List account options
   * @param options
   * @return
   */
  @GatewayAPI
  @API(description = "List account options using AccountSearch")
  public AccessorResponse<MdxList<Account>> list(AccountListOptions options) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List from accounts
   * @return
   */
  @Deprecated
  @GatewayAPI
  @API(description = "List from account options")
  public AccessorResponse<MdxList<Account>> fromAccounts() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List to accounts
   * Notes: Dependent on fromAccount selection
   * @param fromAccountId
   * @return
   */
  @Deprecated
  @GatewayAPI
  @API(description = "List to account options")
  public AccessorResponse<MdxList<Account>> toAccounts(String fromAccountId) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
