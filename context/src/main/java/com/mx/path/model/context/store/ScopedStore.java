package com.mx.path.model.context.store;

import java.util.Objects;
import java.util.Set;

import com.mx.common.store.Store;

public abstract class ScopedStore implements Store {

  private final Store store;

  public ScopedStore(Store store) {
    this.store = store;
  }

  /**
   * Wrap given store in a store scope
   * @param store
   * @param scope
   * @return scoped store
   */
  public static Store build(Store store, String scope) {
    if (Objects.equals("global", scope)) {
      return new ScopedStoreGlobal(store);
    } else if (Objects.equals("client", scope)) {
      return new ScopedStoreClient(store);
    } else if (Objects.equals("user", scope)) {
      return new ScopedStoreCurrentUser(store);
    } else {
      return new ScopedStoreCurrentSession(store);
    }
  }

  /**
   * Add scope to given key
   * @param key
   * @return
   */
  protected abstract String buildKey(String key);

  /**
   * Delete key
   * @param key
   */
  @Override
  public final void delete(String key) {
    store.delete(buildKey(key));
  }

  /**
   * Get value for key
   * @param key
   * @return
   */
  @Override
  public final String get(String key) {
    return store.get(buildKey(key));
  }

  /**
   * Put value
   * @param key
   * @param value
   * @param expirySeconds
   */
  @Override
  public final void put(String key, String value, long expirySeconds) {
    store.put(buildKey(key), value, expirySeconds);
  }

  /**
   * Put value, without a TTL
   * @param key
   * @param value
   */
  @Override
  public final void put(String key, String value) {
    store.put(buildKey(key), value);
  }

  /**
   * Add value to set
   * @param key
   * @param value
   * @param expirySeconds
   */
  @Override
  public void putSet(String key, String value, long expirySeconds) {
    store.putSet(buildKey(key), value, expirySeconds);
  }

  /**
   * Add value to set, without a TTL
   * @param key
   * @param value
   */
  @Override
  public void putSet(String key, String value) {
    store.putSet(buildKey(key), value);
  }

  /**
   * Delete value from set
   * @param key
   * @param value
   */
  @Override
  public void deleteSet(String key, String value) {
    store.deleteSet(buildKey(key), value);
  }

  /**
   * Query whether value is in set
   * @param key
   * @param value
   * @return
   */
  @Override
  public boolean inSet(String key, String value) {
    return store.inSet(buildKey(key), value);
  }

  /**
   * Get set
   * @param key
   * @return
   */
  @Override
  public Set<String> getSet(String key) {
    return store.getSet(buildKey(key));
  }

  /**
   * Set value if key does not already exist
   * @param key
   * @param value
   * @param expirySeconds
   * @return
   */
  @Override
  public final boolean putIfNotExist(String key, String value, long expirySeconds) {
    return store.putIfNotExist(buildKey(key), value, expirySeconds);
  }

  /**
   * Set value if key does not already exist, without a TTL
   * @param key
   * @param value
   * @return
   */
  @Override
  public final boolean putIfNotExist(String key, String value) {
    return store.putIfNotExist(buildKey(key), value);
  }

}
