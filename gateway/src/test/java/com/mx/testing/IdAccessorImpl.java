package com.mx.testing;

import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.testing.accessors.IdBaseAccessor;
import com.mx.testing.model.Authentication;

public class IdAccessorImpl extends IdBaseAccessor {
  public IdAccessorImpl() {
  }

  @Override
  public AccessorResponse<Authentication> authenticate(Authentication authentication) {
    return super.authenticate(authentication);
  }
}
