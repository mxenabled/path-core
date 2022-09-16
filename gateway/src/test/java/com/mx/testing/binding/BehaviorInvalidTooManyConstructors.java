package com.mx.testing.binding;

import lombok.Getter;

import com.mx.common.accessors.AccessorResponse;
import com.mx.common.collections.ObjectMap;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.configuration.annotations.ClientID;
import com.mx.path.gateway.context.GatewayRequestContext;

public class BehaviorInvalidTooManyConstructors extends GatewayBehavior {
  @Getter
  private String clientId;

  public BehaviorInvalidTooManyConstructors(ObjectMap configurations, @ClientID String clientId) {
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
