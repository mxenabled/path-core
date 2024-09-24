package com.mx.path.gateway.event;

import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;

/**
 * Interface for all gateway-emitted events.
 */
public interface GatewayEvent {

  /**
   * Return event request context.
   *
   * @return context
   */
  RequestContext getRequestContext();

  /**
   * Return session.
   *
   * @return session
   */
  Session getSession();
}
