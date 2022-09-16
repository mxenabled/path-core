package com.mx.path.gateway.behavior;

import java.util.function.Function;

import com.mx.common.accessors.AccessorResponse;
import com.mx.common.collections.ObjectMap;
import com.mx.path.gateway.context.GatewayRequestContext;

public class BlockBehavior extends GatewayBehavior {

  private final Function<GatewayRequestContext, AccessorResponse<?>> block;

  public BlockBehavior(Function<GatewayRequestContext, AccessorResponse<?>> block) {
    this(new ObjectMap(), block);
  }

  public BlockBehavior(ObjectMap configurations, Function<GatewayRequestContext, AccessorResponse<?>> block) {
    super(configurations);
    this.block = block;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected final <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return (AccessorResponse<T>) block.apply(request);
  }

}
