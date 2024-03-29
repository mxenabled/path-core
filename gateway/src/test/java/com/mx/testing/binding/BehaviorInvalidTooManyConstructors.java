package com.mx.testing.binding;

import lombok.Getter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.context.GatewayRequestContext;

public class BehaviorInvalidTooManyConstructors extends GatewayBehavior {
  @Getter
  private String clientId;

  public BehaviorInvalidTooManyConstructors(ObjectMap configurations, @Configuration String clientId) {
    this(configurations);
  }

  public BehaviorInvalidTooManyConstructors(ObjectMap configurations) {
    super(configurations);
  }

  @Override
  protected <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return null;
  }
}
