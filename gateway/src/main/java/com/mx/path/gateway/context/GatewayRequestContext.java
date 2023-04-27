package com.mx.path.gateway.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import com.mx.path.core.common.model.ModelBase;
import com.mx.path.core.context.RequestContext;
import com.mx.path.gateway.Gateway;
import com.mx.path.gateway.accessor.Accessor;

/**
 * Decorates RequestContext with Gateway-specific fields.
 *
 * Provides the same API as RequestContext, but will need to be explicitly cast to GatewayRequestContext if accessed
 * via GatewayRequestContext.current() or RequestContext.current()
 */
@SuppressWarnings("RedundantModifier")
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public final class GatewayRequestContext extends RequestContext {
  private Gateway<?> gateway;
  private Accessor currentAccessor;
  private Gateway<?> currentGateway;
  private boolean listOp;
  private Class<? extends ModelBase<?>> model;
  private String op;

  /**
   * Coerces the current RequestContext into a GatewayRequestContext and returns it.
   *
   * @return GatewayRequestContext
   */
  public static GatewayRequestContext current() {
    RequestContext requestContext = RequestContext.current();
    if (requestContext == null) {
      return null;
    }
    return fromRequestContext(requestContext);
  }

  public static GatewayRequestContext fromRequestContext(RequestContext requestContext) {
    if (requestContext instanceof GatewayRequestContext) {
      return (GatewayRequestContext) requestContext;
    } else if (requestContext == null) {
      return GatewayRequestContext.builder().build();
    }
    return new GatewayRequestContext(requestContext);
  }

  private GatewayRequestContext(RequestContext requestContext) {
    super(requestContext.toBuilder());
  }

  /**
   * @return the root gateway
   * @param <T>
   */
  @SuppressWarnings("unchecked")
  public <T extends Gateway<?>> T getGateway() {
    return (T) gateway;
  }

  /**
   * @return the current gateway
   * @param <T>
   */
  @SuppressWarnings("unchecked")
  public <T extends Gateway<?>> T getCurrentGateway() {
    return (T) currentGateway;
  }
}
