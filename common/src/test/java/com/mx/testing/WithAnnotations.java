package com.mx.testing;

import javax.annotation.Nullable;

import com.mx.common.gateway.GatewayClass;

@GatewayClass
public class WithAnnotations {

  @Nullable
  private String aField;

  private String unannotatedField;

  @Nullable
  public String something() {
    return null;
  }

  @Nullable
  public String somethingElse() {
    return null;
  }

  public void somethingCompletelyDifferent() {
  }

  public WithAnnotations(@Nullable String param) {
  }

}
