package com.mx.path.core.context.store;

import com.mx.path.core.context.Session;

/**
 * Interface for management of session persistence.
 */
public interface SessionRepository {

  /**
   * Delete specified session.
   *
   * @param session session to be deleted
   */
  void delete(Session session);

  /**
   * Delete specified key-value pair from session.
   *
   * @param session session to delete key-value pair
   * @param key key to delete
   */
  void deleteValue(Session session, String key);

  /**
   * Retrieve value associated with specified key.
   *
   * @param session session containing key-value pair
   * @param key key
   * @return value associated with specified key
   */
  String getValue(Session session, String key);

  /**
   * Load session associated with specified session ID.
   *
   * @param sessionId session id
   * @return session
   */
  Session load(String sessionId);

  /**
   * Save session.
   *
   * @param session session to save
   */
  void save(Session session);

  /**
   * Set value on key-value pair.
   *
   * @param session session to set key-value pair value
   * @param key key
   * @param value value to set
   */
  void saveValue(Session session, String key, String value);

  /**
   * Add new key-value pair on session if key does not exist.
   *
   * @param session session do add key-value pair
   * @param key key
   * @param value value
   * @return true if key-value pair added
   */
  boolean setIfNotExist(Session session, String key, String value);

  /**
   * Add new key-value pair on session, if key does not exist, and set expiration time.
   *
   * @param session session to add key-value pair
   * @param key key
   * @param value value
   * @param expiryMilliseconds time to expire key-value
   * @return true if key-value pair added
   */
  boolean setIfNotExist(Session session, String key, String value, long expiryMilliseconds);

}
