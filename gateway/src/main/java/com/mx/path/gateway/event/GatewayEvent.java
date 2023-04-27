package com.mx.path.gateway.event;

import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;

/**
 * Interface for all gateway-emitted events
 */
public interface GatewayEvent {

  RequestContext getRequestContext();

  Session getSession();
}
