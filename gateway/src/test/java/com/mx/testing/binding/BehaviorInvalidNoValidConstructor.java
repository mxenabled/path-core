package com.mx.testing.binding;

import lombok.Getter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.context.GatewayRequestContext;

public class BehaviorInvalidNoValidConstructor extends GatewayBehavior {
  @Getter
  private int whatsThis;

  public BehaviorInvalidNoValidConstructor(ObjectMap configurations, int whatsThis) {
    super(configurations);
  }

  @Override
  protected <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return null;
  }
}
