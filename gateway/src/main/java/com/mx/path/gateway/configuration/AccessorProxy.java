package com.mx.path.gateway.configuration;

import com.mx.accessors.Accessor;

public interface AccessorProxy {
  Class<? extends Accessor> getAccessorClass();

  String getScope();
}
