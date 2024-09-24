package com.mx.path.gateway.behavior;

import java.util.function.Function;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.context.GatewayRequestContext;

/**
 * Extended behavior that allow blocking gateway requests.
 */
public class BlockBehavior extends GatewayBehavior {

  private final Function<GatewayRequestContext, AccessorResponse<?>> block;

  /**
   * Build new {@link BlockBehavior} instance with specified block function.
   *
   * @param block block function
   */
  public BlockBehavior(Function<GatewayRequestContext, AccessorResponse<?>> block) {
    this(new ObjectMap(), block);
  }

  /**
   * Build new {@link BlockBehavior} instance with specified configurations and block function.
   *
   * @param configurations configurations
   * @param block block function
   */
  public BlockBehavior(ObjectMap configurations, Function<GatewayRequestContext, AccessorResponse<?>> block) {
    super(configurations);
    this.block = block;
  }

  /**
   * Execute block function on specified request.
   *
   * @param resultType expected return type
   * @param request request to execute
   * @param terminatingBehavior terminating behavior
   * @param <T> result type
   * @return block function response
   */
  @Override
  @SuppressWarnings("unchecked")
  protected final <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return (AccessorResponse<T>) block.apply(request);
  }

}
