package com.mx.path.gateway.event;

import lombok.Builder;
import lombok.Getter;

import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;
import com.mx.path.gateway.Gateway;
import com.mx.path.gateway.accessor.Accessor;

/**
 * Event - Fires after accessor method is invoked.
 */
@Builder
@Getter
public class AfterAccessorEvent implements GatewayEvent, AccessorEvent {

  private final Accessor currentAccessor;
  private final Gateway gateway;
  private final RequestContext requestContext;
  private final Session session;
}
