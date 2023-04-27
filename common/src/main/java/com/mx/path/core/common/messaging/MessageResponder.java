package com.mx.path.core.common.messaging;

/**
 * Capable of receiving and responding to requests.
 */
public interface MessageResponder {
  String respond(String channel, String payload);
}
