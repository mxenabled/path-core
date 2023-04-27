package com.mx.path.gateway.behavior;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.context.GatewayRequestContext;

public class StartBehavior extends GatewayBehavior {

  public StartBehavior() {
    super(new ObjectMap());
  }

  @Override
  protected final <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return callNext(resultType, request, terminatingBehavior);
  }

}
