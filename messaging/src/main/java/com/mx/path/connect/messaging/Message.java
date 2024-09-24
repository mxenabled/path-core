package com.mx.path.connect.messaging;

/**
 * Interface class for messages.
 */
public interface Message {

  /**
   * Return message body.
   *
   * @return message body
   */
  String getBody();

  /**
   * Return message channel.
   *
   * @return message channel
   */
  String getChannel();

  /**
   * Return message headers.
   *
   * @return message headers
   */
  MessageHeaders getMessageHeaders();

  /**
   * Return message parameters.
   *
   * @return message parameters
   */
  MessageParameters getMessageParameters();
}
