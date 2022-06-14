package com.mx.path.gateway.events;

import lombok.Builder;
import lombok.Getter;

import com.mx.accessors.Accessor;
import com.mx.path.gateway.api.Gateway;
import com.mx.path.model.context.RequestContext;

/**
 * Event - Fires after accessor method is invoked.
 */
@Builder
@Getter
public class AfterAccessorEvent implements GatewayEvent, AccessorEvent {

  private final Accessor currentAccessor;
  private final Gateway gateway;
  private final RequestContext requestContext;

}
