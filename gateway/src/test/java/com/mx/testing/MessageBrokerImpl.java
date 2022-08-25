package com.mx.testing;

import lombok.Data;
import lombok.Getter;

import com.mx.common.configuration.Configuration;
import com.mx.common.configuration.ConfigurationField;
import com.mx.common.messaging.EventListener;
import com.mx.common.messaging.MessageBroker;
import com.mx.common.messaging.MessageResponder;
import com.mx.path.gateway.configuration.annotations.ClientID;

public class MessageBrokerImpl implements MessageBroker {
  @Data
  public static class MessageBrokerConfig {
    @ConfigurationField(value = "key1", required = true)
    private String testField;

    @ClientID
    private String clientId;
  }

  @Getter
  MessageBrokerConfig configurations;

  public MessageBrokerImpl(@Configuration MessageBrokerConfig configurations) {
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
