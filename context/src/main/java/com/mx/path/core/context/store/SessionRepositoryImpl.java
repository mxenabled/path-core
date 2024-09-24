package com.mx.path.core.context.store;

import java.time.LocalDateTime;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.core.common.serialization.LocalDateTimeDeserializer;
import com.mx.path.core.common.store.Store;
import com.mx.path.core.context.Session;

/**
 * Implements session repository.
 */
public class SessionRepositoryImpl implements SessionRepository {

  private static final Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, LocalDateTimeDeserializer.builder()
      .build()).create();

  private final Store store;

  /**
   * Build new instance of {@link SessionRepositoryImpl}.
   *
   * @param store store
   */
  public SessionRepositoryImpl(Store store) {
    this.store = store;
  }

  /**
   * Delete session and associated data in store.
   *
   * @param session session
   */
  @Override
  public final void delete(Session session) {
    store.delete(session.getId());
    deleteSessionKeys(session);
  }

  /**
   * Delete session key.
   *
   * @param session session to delete key-value pair
   * @param key key to delete
   */
  @Override
  public final void deleteValue(Session session, String key) {
    getScopedStoreSession(session).delete(key);
  }

  /**
   * Get value from key-value pair.
   *
   * @param session session containing key-value pair
   * @param key key
   * @return value
   */
  @Override
  public final String getValue(Session session, String key) {
    return getScopedStoreSession(session).get(key);
  }

  /**
   * Load session.
   *
   * @param sessionId session id
   * @return session
   */
  @Override
  public final Session load(String sessionId) {
    String json = store.get(sessionId);
    return (json != null) ? GSON.fromJson(json, Session.class) : null;
  }

  /**
   * Keep track of all keys set using session put, sput, putObj, and sputObj.
   *
   * <p>All of these keys will be removed when the session is removed with {@link #delete(Session)}
   * @param session session
   * @param key key to track
   */
  public final void registerSessionKey(Session session, String key) {
    getScopedStoreSession(session).putSet("session_keys", key, session.getExpiresIn());
  }

  /**
   * Save session.
   *
   * @param session session to save
   */
  @Override
  public final void save(Session session) {
    String json = GSON.toJson(session);
    store.put(session.getId(), json, session.getExpiresIn());
  }

  /**
   * Save new key-value pair.
   *
   * @param session session to set key-value pair value
   * @param key key
   * @param value value to set
   */
  @Override
  public final void saveValue(Session session, String key, String value) {
    registerSessionKey(session, key);
    getScopedStoreSession(session).put(key, value, session.getExpiresIn());
  }

  /**
   * Add new key-value pair if it does not already exist.
   *
   * @param session session do add key-value pair
   * @param key key
   * @param value value
   * @return true if key-value pair added.
   */
  @Override
  public final boolean setIfNotExist(Session session, String key, String value) {
    return setIfNotExist(session, key, value, session.getExpiresIn());
  }

  /**
   * Add new key-value pair if it does not already exist.
   *
   * @param session session to add key-value pair
   * @param key key
   * @param value value
   * @param expiryMilliseconds time to expire key-value
   * @return true if key-value pair added.
   */
  @Override
  public final boolean setIfNotExist(Session session, String key, String value, long expiryMilliseconds) {
    return getScopedStoreSession(session).putIfNotExist(key, value, expiryMilliseconds);
  }

  /**
   * Deletes all tracked session keys for given session.
   *
   * <p>Removes all keys found in "session_keys" and then removes the "session_keys" set
   *
   * @param session session
   */
  private void deleteSessionKeys(Session session) {
    Store sessionStore = getScopedStoreSession(session);
    Set<String> keys = sessionStore.getSet("session_keys");
    if (keys != null) {
      keys.forEach(sessionStore::delete);
    }
    sessionStore.delete("session_keys");
  }

  private Store getScopedStoreSession(Session session) {
    return new ScopedStoreSession(this.store, session);
  }
}
