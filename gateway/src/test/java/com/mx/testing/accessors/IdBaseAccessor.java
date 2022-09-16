package com.mx.testing.accessors;

import com.mx.common.accessors.API;
import com.mx.common.accessors.Accessor;
import com.mx.common.accessors.AccessorConfiguration;
import com.mx.common.accessors.AccessorResponse;
import com.mx.common.exception.AccessorMethodNotImplementedException;
import com.mx.common.gateway.GatewayAPI;
import com.mx.common.gateway.GatewayClass;
import com.mx.testing.model.Authentication;

@API(description = "Test authentication accessor")
@GatewayClass
public class IdBaseAccessor extends Accessor {
  public IdBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @GatewayAPI
  @API(description = "Authenticate user")
  public AccessorResponse<Authentication> authenticate(Authentication authentication) {
    throw new AccessorMethodNotImplementedException();
  }
}
