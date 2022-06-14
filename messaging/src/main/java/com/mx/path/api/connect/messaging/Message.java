package com.mx.path.api.connect.messaging;

public interface Message {
  String getBody();

  String getChannel();

  MessageHeaders getMessageHeaders();

  MessageParameters getMessageParameters();
}
