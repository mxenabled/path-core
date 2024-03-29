package com.mx.path.core.context.store;

import com.mx.path.core.context.Session;

public interface SessionRepository {

  void delete(Session session);

  void deleteValue(Session session, String key);

  String getValue(Session session, String key);

  Session load(String sessionId);

  void save(Session session);

  void saveValue(Session session, String key, String value);

  boolean setIfNotExist(Session session, String key, String value);

  boolean setIfNotExist(Session session, String key, String value, long expiryMilliseconds);

}
