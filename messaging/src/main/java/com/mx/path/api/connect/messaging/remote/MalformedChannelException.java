package com.mx.path.api.connect.messaging.remote;

public class MalformedChannelException extends RuntimeException {
  public MalformedChannelException(String channel) {
    this("Malformed channel", channel);
  }

  public MalformedChannelException(String message, String channel) {
    super(message + " (" + channel + ")");
  }
}
