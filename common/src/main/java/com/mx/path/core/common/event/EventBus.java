package com.mx.path.core.common.event;

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

  /**
   * Safely register an event subscriber. Ensure there is at one and only one subscriber of each type. Will create
   * instance of subscriberType if subscriber of the same type does not exist
   *
   * @param subscriberType to register
   */
  default boolean registerByClass(Class<?> subscriberType) {
    throw new UnsupportedOperationException();
  };
}
