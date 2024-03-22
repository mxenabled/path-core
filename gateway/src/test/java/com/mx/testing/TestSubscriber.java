package com.mx.testing;

import com.google.common.eventbus.Subscribe;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;
import com.mx.path.gateway.event.BeforeAccessorEvent;
import com.mx.path.gateway.event.GatewayEvent;

public class TestSubscriber implements GatewayEvent {
  public boolean triggered = false;

  @Subscribe
  public void doSomething(BeforeAccessorEvent event) {
    triggered = true;
  }

  @Override
  public RequestContext getRequestContext() {
    return null;
  }

  @Override
  public Session getSession() {
    return null;
  }
}
