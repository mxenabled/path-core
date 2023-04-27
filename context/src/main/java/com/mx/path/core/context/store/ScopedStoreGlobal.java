package com.mx.path.core.context.store;

import com.mx.path.core.common.store.Store;

public class ScopedStoreGlobal extends ScopedStore {

  public ScopedStoreGlobal(Store store) {
    super(store);
  }

  @Override
  protected final String buildKey(String key) {
    return "global:" + key;
  }

}
