package com.mx.testing;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.testing.accessors.IdBaseAccessor;
import com.mx.testing.model.Authentication;

public class IdAccessorImpl extends IdBaseAccessor {
  public IdAccessorImpl(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public AccessorResponse<Authentication> authenticate(Authentication authentication) {
    return super.authenticate(authentication);
  }
}
