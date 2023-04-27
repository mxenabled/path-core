package com.mx.testing.binding;

import com.mx.path.core.common.accessor.AccessorResponse;
import com.mx.path.core.common.collection.ObjectMap;
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
