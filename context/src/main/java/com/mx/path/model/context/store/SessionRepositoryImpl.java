package com.mx.path.model.context.store;

import com.google.gson.Gson;
import com.mx.common.store.Store;
import com.mx.path.model.context.Session;

public class SessionRepositoryImpl implements SessionRepository {

  private Gson gson = new Gson();
  private Store store;

  public SessionRepositoryImpl(Store store) {
    this.store = store;
  }

  @Override
  public final void delete(Session session) {
    store.delete(session.getId());
  }

  @Override
  public final void deleteValue(Session session, String key) {
    getScopedStoreSession(session).delete(key);
  }

  @Override
  public final String getValue(Session session, String key) {
    return getScopedStoreSession(session).get(key);
  }

  @Override
  public final Session load(String sessionId) {
    String json = store.get(sessionId);
    return (json != null) ? gson.fromJson(json, Session.class) : null;
  }

  @Override
  public final void save(Session session) {
    String json = gson.toJson(session);
    store.put(session.getId(), json, session.getExpiresIn());
  }

  @Override
  public final void saveValue(Session session, String key, String value) {
    getScopedStoreSession(session).put(key, value, session.getExpiresIn());
  }

  @Override
  public final boolean setIfNotExist(Session session, String key, String value) {
    return setIfNotExist(session, key, value, session.getExpiresIn());
  }

  @Override
  public final boolean setIfNotExist(Session session, String key, String value, long expiryMilliseconds) {
    return getScopedStoreSession(session).putIfNotExist(key, value, expiryMilliseconds);
  }

  @Override
  public final String getRedisResponse() {
    return store.status();
  }

  private Store getScopedStoreSession(Session session) {
    return new ScopedStoreSession(this.store, session);
  }

}
