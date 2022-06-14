package com.mx.common.session;

/**
 * SessionStore interface definition
 *
 * MDX sessions support 2 value concepts.
 *   Session Payload - This is a JSON string payload. It contains some static information about the session. This will
 *                     be written on session create
 *   Session Values - These values are key/value pairs associated with the session. They are created, deleted Nd modified
 *                    through the life of a session. They need to exist on their own. The collection should be thread safe
 *                    Each, individual pair is not expected to be thread-safe.
 *
 * @deprecated Abandoning this in favor of the lower-level {@link com.mx.common.store.Store}
 *
 */
@Deprecated
public interface SessionStore {

  /**
   * Delete session
   * @param session
   */
  void delete(SessionInfo session);

  /**
   * Delete key from session
   * @param session
   * @param key
   */
  void deleteValue(SessionInfo session, String key);

  /**
   * Delete key from global session
   * @param key
   */
  void deleteValue(String key);

  /**
   * Get value from session
   * @param session
   * @param key
   * @return
   */
  String getValue(SessionInfo session, String key);

  /**
   * Get value from global session
   * @param key
   * @return
   */
  String getValue(String key);

  /**
   * Get session payload
   * @param sessionId
   * @return
   */
  String load(String sessionId);

  /**
   * Save the session payload
   * @param session
   * @param payload
   */
  void save(SessionInfo session, String payload);

  /**
   * Create/Update a key/value pair belonging to session
   * @param session
   * @param key
   * @param value
   */
  void saveValue(SessionInfo session, String key, String value);

  /**
   * Create/Update a key/value pair belonging to session
   * @param key
   * @param value
   * @param expirySeconds
   */
  void saveValue(String key, String value, long expirySeconds);

  /**
   * Set key/value pair only if the key does not exist
   * @param session
   * @param key
   * @param value
   * @return true, if the value was set
   */
  boolean setIfNotExist(SessionInfo session, String key, String value);

  /**
   * Set key/value pair only if the key does not exist
   * @param session
   * @param key
   * @param value
   * @param expirySeconds
   * @return true, if the value was set
   */
  boolean setIfNotExist(SessionInfo session, String key, String value, long expirySeconds);

  /**
   * Set key/value pair only if the key does not exist
   * @param key
   * @param value
   * @param expirySeconds
   * @return true, if the value was set
   */
  boolean setIfNotExist(String key, String value, long expirySeconds);

  /**
   * @return OK if the repository is available
   */
  default String status() {
    return "OK";
  }
}
