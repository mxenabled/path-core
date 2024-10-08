package com.mx.path.gateway.event;

import com.google.common.eventbus.Subscribe;

/**
 * An instance of this noop handler is registered on all GatewayEventBus instances.
 * All events are required to implement {@link GatewayEvent} and/or {@link AccessorEvent}
 * This event handler makes sure there is at least one subscriber for all events, preventing
 * events from ending up being retried. See {@link com.google.common.eventbus.EventBus} for
 * details.
 */
public class DefaultEventHandler {

  /**
   * Noop subscriber for all {@link AccessorEvent} events.
   *
   * @param event event
   */
  @Subscribe
  public void handleAccessorEvent(AccessorEvent event) {
  }

  /**
   * Noop subscriber for all {@link GatewayEvent} events.
   *
   * @param event event
   */
  @Subscribe
  public void handleGatewayEvent(GatewayEvent event) {
  }

}
