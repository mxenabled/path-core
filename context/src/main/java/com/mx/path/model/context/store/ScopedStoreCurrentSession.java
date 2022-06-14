package com.mx.path.model.context.store;

import com.mx.common.store.Store;
import com.mx.path.model.context.GatewayContextException;
import com.mx.path.model.context.Session;

public class ScopedStoreCurrentSession extends ScopedStore {

  public ScopedStoreCurrentSession(Store store) {
    super(store);
  }

  @Override
  protected final String buildKey(String key) {
    if (Session.current() == null) {
      throw new GatewayContextException("Attempting to read id from null Session");
    } else if (Session.current().getId() == null) {
      throw new GatewayContextException("Session#id must be non-null to use ScopedStoreCurrentSession");
    }

    return Session.current().getId() + ":" + key;
  }

}
