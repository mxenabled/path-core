package com.mx.accessors.remote_deposit;

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
import com.mx.models.remote_deposit.RemoteDeposit;

/**
 * Accessor base for remote deposits
 *
 * <p>See <a href="https://developer.mx.com/drafts/mdx/remote_deposit/#mdx-remote-deposit">specifications</a>
 */
@GatewayClass
@API(specificationUrl = "https://developer.mx.com/drafts/mdx/remote_deposit/#mdx-remote-deposit")
public abstract class RemoteDepositBaseAccessor extends Accessor {

  public RemoteDepositBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Create a remote deposit
   * @param remoteDeposit
   * @return
   */
  @GatewayAPI
  @API(description = "Create a remote deposit")
  public AccessorResponse<RemoteDeposit> create(RemoteDeposit remoteDeposit) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * Get a remote deposit
   * @param id
   * @return
   */
  @GatewayAPI
  @API(description = "Get a remote deposit")
  public AccessorResponse<RemoteDeposit> get(String id) {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List deposit accounts for remote deposits
   * @return
   */
  @GatewayAPI
  @API(description = "List deposit accounts for remote deposits")
  public AccessorResponse<MdxList<Account>> accounts() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

  /**
   * List all remote deposits
   * @return
   */
  @GatewayAPI
  @API(description = "List all remote deposits")
  public AccessorResponse<MdxList<RemoteDeposit>> list() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }

}
