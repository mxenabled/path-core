package com.mx.path.testing.session;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.context.Session;
import com.mx.path.core.context.store.SessionRepository;

/**
 * Stand-in session repository using in-memory HashMap.
 */
public class TestSessionRepository implements SessionRepository {
  private Map<String, String> values = new HashMap<>();

  @Override
  public final void delete(Session session) {
    values.remove(session.getId());
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
    String json = values.get(sessionId);
    if (Strings.isBlank(json)) {
      return null;
    }

    return new Gson().fromJson(json, Session.class);
  }

  @Override
  public final void save(Session session) {
    values.put(session.getId(), new Gson().toJson(session));
  }

  @Override
  public final void saveValue(Session session, String key, String value) {
    values.put(session.getId() + "." + key, value);
  }

  @Override
  public final boolean setIfNotExist(Session session, String key, String value) {
    if (!values.containsKey(session.getId() + "." + key)) {
      saveValue(session, key, value);
      return true;
    }

    return false;
  }

  @Override
  public final boolean setIfNotExist(Session session, String key, String value, long expiryMilliseconds) {
    if (!values.containsKey(session.getId() + "." + key)) {
      saveValue(session, key, value);
      return true;
    }

    return false;
  }
}
