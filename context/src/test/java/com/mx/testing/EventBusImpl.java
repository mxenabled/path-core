package com.mx.testing;

import lombok.Getter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.event.EventBus;

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
