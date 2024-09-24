package com.mx.path.core.context.store;

import com.mx.path.core.common.store.Store;
import com.mx.path.core.context.GatewayContextException;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;

/**
 * Extends store to provide additional functionality for storage of session keys.
 */
public class ScopedStoreSession extends ScopedStore {

  private final Session session;

  /**
   * Build new {@link ScopedStoreSession} instance with specified parameters.
   *
   * @param store store
   * @param session session
   */
  public ScopedStoreSession(Store store, Session session) {
    super(store);
    this.session = session;
  }

  /**
   * Build new key with session scope.
   *
   * @param key key to add to scope
   * @return key
   */
  @Override
  protected final String buildKey(String key) {
    if (session != null) {
      return session.getId() + ":" + key;
    }

    throw new GatewayContextException("Attempting to read key from null session for client: " + RequestContext.current().getClientId());
  }

}
