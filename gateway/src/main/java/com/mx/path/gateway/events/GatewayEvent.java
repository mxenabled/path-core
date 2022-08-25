package com.mx.path.gateway.events;

import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.Session;

/**
 * Interface for all gateway-emitted events
 */
public interface GatewayEvent {

  RequestContext getRequestContext();

  Session getSession();
}
