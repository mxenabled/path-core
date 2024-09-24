package com.mx.path.core.common.messaging;

/**
 * Capable of receiving and responding to requests.
 */
public interface MessageResponder {

  /**
   * Respond request.
   *
   * @param channel channel to respond
   * @param payload paylad of request
   * @return response
   */
  String respond(String channel, String payload);
}
