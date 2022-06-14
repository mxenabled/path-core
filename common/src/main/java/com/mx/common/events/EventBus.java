package com.mx.common.events;

/**
 * Represents a publish/subscribe event bus that is intended for local events.
 *
 * Operations:
 *
 * - Post an event
 * - Register an event subscriber
 */
public interface EventBus {
  /**
   * Post an event to the event bus
   *
   * @param event
   */
  void post(Object event);

  /**
   * Register an event subscriber
   *
   * @param subscriber to register
   */
  void register(Object subscriber);
}
