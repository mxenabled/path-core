package com.mx.testing;

import javax.annotation.Nullable;

import com.mx.path.core.common.gateway.GatewayClass;

@GatewayClass
public class WithAnnotationsBase extends WithAnnotationsBaseBase {

  @Nullable
  private String baseField;
}
