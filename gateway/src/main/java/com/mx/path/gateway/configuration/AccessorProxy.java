package com.mx.path.gateway.configuration;

import com.mx.common.accessors.Accessor;

public interface AccessorProxy {
  Class<? extends Accessor> getAccessorClass();

  String getScope();

  <T extends Accessor> T build();
}
