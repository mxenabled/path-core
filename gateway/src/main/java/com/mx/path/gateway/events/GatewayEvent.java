package com.mx.path.gateway.events;

import com.mx.path.model.context.RequestContext;

/**
 * Interface for all gateway-emitted events
 */
public interface GatewayEvent {
  RequestContext getRequestContext();
}
