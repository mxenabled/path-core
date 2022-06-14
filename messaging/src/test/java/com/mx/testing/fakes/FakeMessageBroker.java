package com.mx.testing.fakes;

import lombok.Getter;

import com.mx.common.collections.ObjectMap;
import com.mx.common.messaging.EventListener;
import com.mx.common.messaging.MessageBroker;
import com.mx.common.messaging.MessageResponder;

public class FakeMessageBroker implements MessageBroker {
  @Getter
  private final ObjectMap configurations;

  public FakeMessageBroker(ObjectMap configurations) {
    this.configurations = configurations;
  }

  @Override
  public String request(String channel, String payload) {
    return "response";
  }

  @Override
  public void publish(String channel, String payload) {

  }

  @Override
  public void registerResponder(String channel, MessageResponder responder) {

  }

  @Override
  public void registerListener(String channel, EventListener listener) {

  }
}
