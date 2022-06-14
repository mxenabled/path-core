package com.mx.path.model.context.store;

import com.mx.common.store.Store;
import com.mx.path.model.context.GatewayContextException;
import com.mx.path.model.context.RequestContext;

public class ScopedStoreClient extends ScopedStore {

  public ScopedStoreClient(Store store) {
    super(store);
  }

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
