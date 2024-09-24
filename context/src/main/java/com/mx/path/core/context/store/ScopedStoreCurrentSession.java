package com.mx.path.core.context.store;

import com.mx.path.core.common.store.Store;
import com.mx.path.core.context.GatewayContextException;
import com.mx.path.core.context.Session;

/**
 * Extends store to provide additional functionality for storage of session keys.
 */
public class ScopedStoreCurrentSession extends ScopedStore {

  /**
   * Build new {@link ScopedStoreSession} instance with specified store.
   *
   * @param store store
   */
  public ScopedStoreCurrentSession(Store store) {
    super(store);
  }

  /**
   * Build new key with session scope.
   *
   * @param key key to add to scope
   * @return key
   */
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
