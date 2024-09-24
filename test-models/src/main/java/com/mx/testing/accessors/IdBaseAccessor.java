package com.mx.testing.accessors;

import com.mx.path.core.common.accessor.API;
import com.mx.path.core.common.accessor.AccessorMethodNotImplementedException;
import com.mx.path.core.common.gateway.GatewayAPI;
import com.mx.path.core.common.gateway.GatewayClass;
import com.mx.path.gateway.accessor.Accessor;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.testing.model.Authentication;

/**
 * Test class for id accessor.
 */
@API(description = "Test authentication accessor")
@GatewayClass
public abstract class IdBaseAccessor extends Accessor {

  /**
   * Default constructor.
   */
  public IdBaseAccessor() {
  }

  /**
   * Authenticate.
   * @param authentication data for authentication
   * @return authentication
   */
  @GatewayAPI
  @API(description = "Authenticate user")
  public AccessorResponse<Authentication> authenticate(Authentication authentication) {
    throw new AccessorMethodNotImplementedException();
  }
}
