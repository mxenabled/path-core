package com.mx.testing;

import lombok.Getter;

import com.mx.common.collections.ObjectMap;
import com.mx.common.events.EventBus;

public class EventBusImpl implements EventBus {
  @Getter
  private final ObjectMap configurations;

  public EventBusImpl(ObjectMap configurations) {
    this.configurations = configurations;
  }

  @Override
  public void post(Object event) {

  }

  @Override
  public void register(Object subscriber) {

  }
}
