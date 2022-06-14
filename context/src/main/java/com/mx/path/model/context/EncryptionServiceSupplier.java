package com.mx.path.model.context;

import java.util.Objects;
import java.util.function.Supplier;

import com.mx.common.security.EncryptionService;
import com.mx.path.model.context.facility.Facilities;

public class EncryptionServiceSupplier implements Supplier<EncryptionService> {
  @Override
  public final EncryptionService get() {
    RequestContext requestContext = RequestContext.current();

    if (Objects.isNull(requestContext)) {
      return null;
    }

    return Facilities.getEncryptionService(requestContext.getClientId());
  }
}
