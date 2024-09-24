package com.mx.path.gateway.behavior;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.context.GatewayRequestContext;

/**
 * Abstract base class to define gateway behaviors.
 */
public abstract class GatewayBehavior {

  /**
   * Return configurations.
   *
   * @return configurations
   */
  @Getter
  private ObjectMap configurations;

  /**
   * -- GETTER --
   * Return chained behavior.
   *
   * @return chained behavior
   * -- SETTER --
   * Set chained behavior.
   *
   * @param nextBehavior chained behavior to set
   */
  @Setter
  @Getter
  private GatewayBehavior nextBehavior;

  /**
   * Build new {@link GatewayBehavior} instance with specified configurations.
   *
   * @param configurations configurations
   */
  public GatewayBehavior(ObjectMap configurations) {
    this.configurations = configurations;
  }

  /**
   * Build new {@link GatewayBehavior} instance with specified configurations and next behavior.
   *
   * @param configurations configurations
   * @param nextBehavior next behavior
   */
  public GatewayBehavior(ObjectMap configurations, GatewayBehavior nextBehavior) {
    this(configurations);
    this.nextBehavior = nextBehavior;
  }

  /**
   * Execute the behavior stack.
   *
   * @param resultType type of result
   * @param request request
   * @param terminatingBehavior chained behavior
   * @param <T> result type
   * @return result
   */
  public <T> AccessorResponse<T> execute(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return call(resultType, request, terminatingBehavior);
  }

  /**
   * Do the work for this behavior. Call callNext() to continue the chain.
   *
   * @param resultType type of result
   * @param request request to execute behavior
   * @param terminatingBehavior next behavior
   * @param <T> type of result
   * @return result
   */
  protected abstract <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior);

  /**
   * Calls next behavior unless null.
   *
   * @param resultType type of result
   * @param request request to execute behavior
   * @param terminatingBehavior next behavior
   * @param <T> type of result
   * @return result
   */
  protected <T> AccessorResponse<T> callNext(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    if (nextBehavior != null) {
      return nextBehavior.call(resultType, request, terminatingBehavior);
    }

    return terminatingBehavior.call(resultType, request, terminatingBehavior);
  }

  /**
   * Describe this behavior.
   *
   * @return description
   */
  public ObjectMap describe() {
    ObjectMap description = new ObjectMap();
    description.put("class", getClass().getSimpleName());

    if (configurations != null && !configurations.isEmpty()) {
      description.put("configurations", configurations);
    }

    return description;
  }
}
