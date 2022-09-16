package com.mx.path.gateway.behavior;

import lombok.Getter;
import lombok.Setter;

import com.mx.common.accessors.AccessorResponse;
import com.mx.common.collections.ObjectMap;
import com.mx.path.gateway.context.GatewayRequestContext;

public abstract class GatewayBehavior {
  @Getter
  private ObjectMap configurations;

  @Setter
  @Getter
  private GatewayBehavior nextBehavior;

  public GatewayBehavior(ObjectMap configurations) {
    this.configurations = configurations;
  }

  public GatewayBehavior(ObjectMap configurations, GatewayBehavior nextBehavior) {
    this(configurations);
    this.nextBehavior = nextBehavior;
  }

  /**
   * Execute the behavior stack
   * @param request
   */
  public <T> AccessorResponse<T> execute(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return call(resultType, request, terminatingBehavior);
  }

  /**
   * Do the work for this behavior. Call callNext() to continue the chain.
   */
  protected abstract <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior);

  /**
   * Calls next behavior unless null
   */
  protected <T> AccessorResponse<T> callNext(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    if (nextBehavior != null) {
      return nextBehavior.call(resultType, request, terminatingBehavior);
    }

    return terminatingBehavior.call(resultType, request, terminatingBehavior);
  }

  /**
   * Describe this behavior
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
