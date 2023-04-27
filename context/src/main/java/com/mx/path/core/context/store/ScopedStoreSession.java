package com.mx.path.core.context.store;

import com.mx.path.core.common.store.Store;
import com.mx.path.core.context.GatewayContextException;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;

public class ScopedStoreSession extends ScopedStore {

  private final Session session;

  public ScopedStoreSession(Store store, Session session) {
    super(store);
    this.session = session;
  }

  @Override
  protected final String buildKey(String key) {
    if (session != null) {
      return session.getId() + ":" + key;
    }

    throw new GatewayContextException("Attempting to read key from null session for client: " + RequestContext.current().getClientId());
  }

}
