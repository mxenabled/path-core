package com.mx.path.core.context.store;

import com.mx.path.core.common.store.Store;
import com.mx.path.core.context.GatewayContextException;
import com.mx.path.core.context.RequestContext;

/**
 * Extends store to provide additional functionality for storage of client keys.
 */
public class ScopedStoreClient extends ScopedStore {

  /**
   * Build new {@link ScopedStoreClient} instance with specified store.
   *
   * @param store store
   */
  public ScopedStoreClient(Store store) {
    super(store);
  }

  /**
   * Build new key with client scope.
   *
   * @param key key to add to scope
   * @return key
   */
  @Override
  protected final String buildKey(String key) {
    if (RequestContext.current() == null) {
      throw new GatewayContextException("Attempting to read clientId from null RequestContext");
    } else if (RequestContext.current().getClientId() == null) {
      throw new GatewayContextException("RequestContext#clientId must be non-null to use ScopedStoreClient");
    }

    return RequestContext.current().getClientId() + ":" + key;
  }

}
