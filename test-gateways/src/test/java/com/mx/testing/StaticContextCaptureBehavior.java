package com.mx.testing;

import com.mx.path.core.common.accessor.AccessorResponse;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.context.GatewayRequestContext;

/**
 * Used in tests to observe context as requests pass through the gateway.
 */
public class StaticContextCaptureBehavior extends GatewayBehavior {
  public static GatewayRequestContext capturedContext;

  public static void reset() {
    capturedContext = null;
  }

  public StaticContextCaptureBehavior(ObjectMap configurations) {
    super(configurations);
  }

  public StaticContextCaptureBehavior(ObjectMap configurations, GatewayBehavior nextBehavior) {
    super(configurations, nextBehavior);
  }

  @Override
  protected <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    StaticContextCaptureBehavior.capturedContext = request;

    return callNext(resultType, request, terminatingBehavior);
  }
}
