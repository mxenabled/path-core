package com.mx.path.core.common.messaging;

/**
 * Capable of receiving events. Does not respond to events.
 */
public interface EventListener {
  void receive(String channel, String payload);
}
