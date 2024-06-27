package com.mx.testing.accessors;

import lombok.Getter;

import com.mx.path.core.common.accessor.API;
import com.mx.path.core.common.accessor.AccessorMethodNotImplementedException;
import com.mx.path.core.common.accessor.RootAccessor;
import com.mx.path.core.common.gateway.GatewayAPI;
import com.mx.path.gateway.accessor.Accessor;

@API(description = "Base Gateway Accessor that serves as the main entrypoint to all other Gateway Accessors", specificationUrl = "https://developer.mx.com/drafts/mdx/overview/#what-is-helios")
@RootAccessor
public class BaseAccessor extends Accessor {

  @GatewayAPI
  @Getter
  private AccountBaseAccessor accounts;

  @GatewayAPI
  @Getter
  private IdBaseAccessor id;

  public BaseAccessor() {
  }

  /**
   * Accessor for account operations
   *
   * @return accessor
   */
  @API
  public AccountBaseAccessor accounts() {
    if (accounts != null) {
      return accounts;
    }

    throw new AccessorMethodNotImplementedException();
  }

  /**
   * Accessor for account operations
   *
   * @return accessor
   */
  @API
  public IdBaseAccessor id() {
    if (id != null) {
      return id;
    }

    throw new AccessorMethodNotImplementedException();
  }
}
