package com.mx.testing.binding;

import com.mx.accessors.AccessorResponse;
import com.mx.common.collections.ObjectMap;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.context.GatewayRequestContext;

public class BehaviorWithNoArgs extends GatewayBehavior {

  @Override
  protected <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return null;
  }

  public BehaviorWithNoArgs() {
    super(new ObjectMap());
  }
}
