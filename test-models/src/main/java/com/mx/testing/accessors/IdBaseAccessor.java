package com.mx.testing.accessors;

import com.mx.path.core.common.accessor.API;
import com.mx.path.core.common.accessor.Accessor;
import com.mx.path.core.common.accessor.AccessorConfiguration;
import com.mx.path.core.common.accessor.AccessorMethodNotImplementedException;
import com.mx.path.core.common.accessor.AccessorResponse;
import com.mx.path.core.common.gateway.GatewayAPI;
import com.mx.path.core.common.gateway.GatewayClass;
import com.mx.testing.model.Authentication;

@API(description = "Test authentication accessor")
@GatewayClass
public abstract class IdBaseAccessor extends Accessor {
  public IdBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Authenticate
   * @param authentication
   * @return
   */
  @GatewayAPI
  @API(description = "Authenticate user")
  public AccessorResponse<Authentication> authenticate(Authentication authentication) {
    throw new AccessorMethodNotImplementedException();
  }
}
