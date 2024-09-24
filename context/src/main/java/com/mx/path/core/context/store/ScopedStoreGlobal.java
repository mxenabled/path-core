package com.mx.path.core.context.store;

import com.mx.path.core.common.store.Store;

/**
 * Extends store to provide additional functionality for storage of global keys.
 */
public class ScopedStoreGlobal extends ScopedStore {

  /**
   * Build new {@link ScopedStoreGlobal} instance with specified store.
   *
   * @param store store
   */
  public ScopedStoreGlobal(Store store) {
    super(store);
  }

  /**
   * Build new key with global scope.
   *
   * @param key key to add to scope
   * @return key
   */
  @Override
  protected final String buildKey(String key) {
    return "global:" + key;
  }

}
