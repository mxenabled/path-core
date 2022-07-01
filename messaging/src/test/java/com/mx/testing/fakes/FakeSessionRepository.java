package com.mx.testing.fakes;

import java.util.HashMap;
import java.util.Map;

import com.mx.path.model.context.Session;
import com.mx.path.model.context.store.SessionRepository;

public class FakeSessionRepository implements SessionRepository {
  private Map<String, String> values = new HashMap<>();

  @Override
  public final void delete(Session session) {
  }

  @Override
  public final void deleteValue(Session session, String key) {
    values.remove(session.getId() + "." + key);
  }

  @Override
  public final String getValue(Session session, String key) {
    return values.get(session.getId() + "." + key);
  }

  @Override
  public final Session load(String sessionId) {
    Session session = new Session();
    session.setId(sessionId);

    return session;
  }

  @Override
  public final void save(Session session) {
  }

  @Override
  public final void saveValue(Session session, String key, String value) {
    values.put(session.getId() + "." + key, value);
  }

  @Override
  public final boolean setIfNotExist(Session session, String key, String value) {
    if (values.containsKey(session.getId() + "." + key)) {
      return false;
    }
    saveValue(session, key, value);

    return true;
  }

  @Override
  public final boolean setIfNotExist(Session session, String key, String value, long expiryMilliseconds) {
    return setIfNotExist(session, key, value);
  }

}
