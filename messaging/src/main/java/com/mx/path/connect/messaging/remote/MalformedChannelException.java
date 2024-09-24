package com.mx.path.connect.messaging.remote;

/**
 * Exception for malformed channel names.
 */
public class MalformedChannelException extends RuntimeException {

  /**
   * Build new {@link MalformedChannelException} instance with a default exception message.
   *
   * @param channel malformed channel that caused the exception
   */
  public MalformedChannelException(String channel) {
    this("Malformed channel", channel);
  }

  /**
   * Build new {@link MalformedChannelException} with custom message and channel.
   *
   * @param message exception message
   * @param channel malformed channel that caused the exception
   */
  public MalformedChannelException(String message, String channel) {
    super(message + " (" + channel + ")");
  }
}
