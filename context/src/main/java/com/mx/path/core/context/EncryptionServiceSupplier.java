package com.mx.path.core.context;

import java.util.Objects;
import java.util.function.Supplier;

import com.mx.path.core.common.security.EncryptionService;
import com.mx.path.core.context.facility.Facilities;

/**
 * Utility class to safely get encryption services.
 */
public class EncryptionServiceSupplier implements Supplier<EncryptionService> {

  /**
   * Get encryption service.
   *
   * @return return encryption service, null if context not defined
   */
  @Override
  public final EncryptionService get() {
    RequestContext requestContext = RequestContext.current();

    if (Objects.isNull(requestContext)) {
      return null;
    }

    return Facilities.getEncryptionService(requestContext.getClientId());
  }
}
