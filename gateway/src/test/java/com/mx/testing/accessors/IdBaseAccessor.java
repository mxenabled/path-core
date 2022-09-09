package com.mx.testing.accessors;

import com.mx.accessors.API;
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorException;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
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
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
