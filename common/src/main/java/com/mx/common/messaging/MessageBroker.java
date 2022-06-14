package com.mx.common.messaging;

/**
 * Represents a subscriber/publisher message broker.
 *
 * Operations:
 *
 * - Subscribe a message responder
 * - Subscribe an event listener
 * - Send a request
 * - Publish an event
 */
public interface MessageBroker {
  /**
   * Send/receive data over channel
   * @param channel to send request
   * @param payload of the request
   * @return response to message
   */
  String request(String channel, String payload);

  /**
   * Publish event to channel
   * @param channel to send event
   * @param payload of the event
   */
  void publish(String channel, String payload);

  /**
   * Register a server-side responder for incoming requests
   * @param channel to listen on
   * @param responder implementation
   */
  void registerResponder(String channel, MessageResponder responder);

  /**
   * Register server-side event listener
   * @param channel to listen on
   * @param listener implementation
   */
  void registerListener(String channel, EventListener listener);
}
