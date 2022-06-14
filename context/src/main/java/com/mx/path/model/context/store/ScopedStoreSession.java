package com.mx.path.model.context.store;

import com.mx.common.store.Store;
import com.mx.path.model.context.GatewayContextException;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.Session;

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
