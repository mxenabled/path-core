package com.mx.path.connect.messaging;

public interface Message {
  String getBody();

  String getChannel();

  MessageHeaders getMessageHeaders();

  MessageParameters getMessageParameters();
}
