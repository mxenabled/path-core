package com.mx.testing.accessors;

import lombok.Getter;

import com.mx.common.accessors.API;
import com.mx.common.accessors.Accessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.RootAccessor;
import com.mx.common.exception.AccessorMethodNotImplementedException;
import com.mx.common.gateway.GatewayAPI;

@API(description = "Base Gateway Accessor that serves as the main entrypoint to all other Gateway Accessors", specificationUrl = "https://developer.mx.com/drafts/mdx/overview/#what-is-helios")
@RootAccessor
public class BaseAccessor extends Accessor {

  @GatewayAPI
  @Getter
  private AccountBaseAccessor accounts;

  @GatewayAPI
  @Getter
  private IdBaseAccessor id;

  public BaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Accessor for account operations
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
