package com.mx.path.core.context.store;

import com.mx.path.core.common.store.Store;
import com.mx.path.core.context.GatewayContextException;
import com.mx.path.core.context.Session;

/**
 * Extends store to provide additional functionality for storage of user session keys.
 */
public class ScopedStoreCurrentUser extends ScopedStore {

  /**
   * Build new {@link ScopedStoreCurrentUser} instance with specified store.
   *
   * @param store store
   */
  public ScopedStoreCurrentUser(Store store) {
    super(store);
  }

  /**
   * Build new key with user scope.
   *
   * @param key key to add to scope
   * @return key
   */
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
