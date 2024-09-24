package com.mx.testing.accessors;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.accessor.API;
import com.mx.path.core.common.accessor.AccessorMethodNotImplementedException;
import com.mx.path.core.common.accessor.RootAccessor;
import com.mx.path.core.common.gateway.GatewayAPI;
import com.mx.path.gateway.accessor.Accessor;

/**
 * Test class for base accessor.
 */
@API(description = "Base Gateway Accessor that serves as the main entrypoint to all other Gateway Accessors", specificationUrl = "https://developer.mx.com/drafts/mdx/overview/#what-is-helios")
@RootAccessor
public class BaseAccessor extends Accessor {

  /**
   * -- GETTER --
   * Return account accessor.
   *
   * @return account accessor
   *
   * -- SETTER --
   * Set account accessor.
   *
   * @param accounts account accessor
   */
  @GatewayAPI
  @Getter
  @Setter
  private AccountBaseAccessor accounts;

  /**
   * -- GETTER --
   * Return accessor id.
   *
   * @return accessor id
   *
   * -- SETTER --
   * Set accessor id.
   *
   * @param id id to set
   */
  @GatewayAPI
  @Getter
  @Setter
  private IdBaseAccessor id;

  /**
   * Default constructor.
   */
  public BaseAccessor() {
  }

  /**
   * Accessor for account operations.
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
   * Accessor for account operations.
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
