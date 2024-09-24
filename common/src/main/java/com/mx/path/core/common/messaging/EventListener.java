package com.mx.path.core.common.messaging;

/**
 * Capable of receiving events. Does not respond to events.
 */
public interface EventListener {

  /**
   * Receive new event.
   *
   * @param channel event channel
   * @param payload event payload
   */
  void receive(String channel, String payload);
}
