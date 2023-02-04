package com.mx.path.model.context;

/**
 * Interface for all gateway-emitted events
 */
public interface GatewayEvent {

  RequestContext getRequestContext();

  Session getSession();
}
