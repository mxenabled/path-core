package com.mx.path.core.context;

import java.util.Objects;
import java.util.function.Supplier;

import com.mx.path.core.common.store.Store;
import com.mx.path.core.context.facility.Facilities;
import com.mx.path.core.context.store.SessionRepository;
import com.mx.path.core.context.store.SessionRepositoryImpl;

public class SessionRepositorySupplier implements Supplier<SessionRepository> {
  @Override
  public final SessionRepository get() {
    RequestContext requestContext = RequestContext.current();

    if (Objects.isNull(requestContext)) {
      return null;
    }

    Store store = Facilities.getSessionStore(requestContext.getClientId());
    return new SessionRepositoryImpl(store);
  }
}
