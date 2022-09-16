package com.mx.testing.binding;

import lombok.Data;

import com.mx.common.accessors.AccessorResponse;
import com.mx.common.collections.ObjectMap;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.context.GatewayRequestContext;

@Data
public class BehaviorWithObjectMap extends GatewayBehavior {

  private ObjectMap objectMap;

  public BehaviorWithObjectMap(ObjectMap configurations) {
    super(configurations);
    this.objectMap = configurations;
  }

  @Override
  protected <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return null;
  }
}
