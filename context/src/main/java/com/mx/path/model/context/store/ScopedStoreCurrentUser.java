package com.mx.path.model.context.store;

import com.mx.common.store.Store;
import com.mx.path.model.context.GatewayContextException;
import com.mx.path.model.context.Session;

public class ScopedStoreCurrentUser extends ScopedStore {

  public ScopedStoreCurrentUser(Store store) {
    super(store);
  }

  @Override
  protected final String buildKey(String key) {
    if (Session.current() == null) {
      throw new GatewayContextException("Attempting to read userId from null Session");
    } else if (Session.current().getUserId() == null) {
      throw new GatewayContextException("Session#userId must be non-null to use ScopedStoreCurrentUser");
    }

    return Session.current().getUserId() + ":" + key;
  }

}
