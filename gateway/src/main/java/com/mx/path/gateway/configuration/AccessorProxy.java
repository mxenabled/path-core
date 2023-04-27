package com.mx.path.gateway.configuration;

import com.mx.path.core.common.accessor.Accessor;

public interface AccessorProxy {
  Class<? extends Accessor> getAccessorClass();

  String getScope();

  <T extends Accessor> T build();
}
