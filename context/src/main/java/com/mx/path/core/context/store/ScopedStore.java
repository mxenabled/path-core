package com.mx.path.core.context.store;

import java.util.Objects;
import java.util.Set;

import com.mx.path.core.common.store.Store;

/**
 * Abstract class to provide scope logic to base store.
 */
public abstract class ScopedStore implements Store {

  private final Store store;

  /**
   * Build new {@link ScopedStore} instance and set self store.
   *
   * @param store store to set
   */
  public ScopedStore(Store store) {
    this.store = store;
  }

  /**
   * Wrap given store in a store scope.
   *
   * @param store store to wrap
   * @param scope store scope
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
   * Add scope to given key.
   *
   * @param key key to add scope
   * @return scoped key
   */
  protected abstract String buildKey(String key);

  /**
   * Delete key.
   *
   * @param key key to delete scope.
   */
  @Override
  public final void delete(String key) {
    store.delete(buildKey(key));
  }

  /**
   * Get value for key.
   *
   * @param key key to get value from
   * @return value
   */
  @Override
  public final String get(String key) {
    return store.get(buildKey(key));
  }

  /**
   * Put new key value.
   *
   * @param key key
   * @param value value
   * @param expirySeconds seconds to expire new key value
   */
  @Override
  public final void put(String key, String value, long expirySeconds) {
    store.put(buildKey(key), value, expirySeconds);
  }

  /**
   * Put value, without a TTL.
   *
   * @param key key
   * @param value value
   */
  @Override
  public final void put(String key, String value) {
    store.put(buildKey(key), value);
  }

  /**
   * Add value to set.
   *
   * @param key key
   * @param value value
   * @param expirySeconds seconds to expire
   */
  @Override
  public void putSet(String key, String value, long expirySeconds) {
    store.putSet(buildKey(key), value, expirySeconds);
  }

  /**
   * Add value to set, without a TTL.
   *
   * @param key key
   * @param value value
   */
  @Override
  public void putSet(String key, String value) {
    store.putSet(buildKey(key), value);
  }

  /**
   * Delete value from set.
   *
   * @param key key
   * @param value value
   */
  @Override
  public void deleteSet(String key, String value) {
    store.deleteSet(buildKey(key), value);
  }

  /**
   * Query whether value is in set.
   *
   * @param key key
   * @param value value
   * @return true, if value in set
   */
  @Override
  public boolean inSet(String key, String value) {
    return store.inSet(buildKey(key), value);
  }

  /**
   * Get set.
   *
   * @param key key
   * @return set
   */
  @Override
  public Set<String> getSet(String key) {
    return store.getSet(buildKey(key));
  }

  /**
   * Set value if key does not already exist.
   *
   * @param key key
   * @param value value
   * @param expirySeconds seconds to expire
   * @return true, if a value was set
   */
  @Override
  public final boolean putIfNotExist(String key, String value, long expirySeconds) {
    return store.putIfNotExist(buildKey(key), value, expirySeconds);
  }

  /**
   * Set value if key does not already exist, without a TTL.
   *
   * @param key key
   * @param value value
   * @return true, if a value was set
   */
  @Override
  public final boolean putIfNotExist(String key, String value) {
    return store.putIfNotExist(buildKey(key), value);
  }
}
