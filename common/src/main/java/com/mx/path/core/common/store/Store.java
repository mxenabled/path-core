package com.mx.path.core.common.store;

import java.util.Set;

/**
 * Key-Value Store
 */
public interface Store {

  /**
   * Delete key.
   *
   * @param key key
   */
  void delete(String key);

  /**
   * Delete a value from a set.
   *
   * @param key key
   * @param value value
   */
  void deleteSet(String key, String value);

  /**
   * Get value.
   *
   * @param key key
   * @return value
   */
  String get(String key);

  /**
   * Read a set.
   *
   * @param key key
   * @return set
   */
  Set<String> getSet(String key);

  /**
   * Query set for existence of a value.
   *
   * @param key key
   * @param value value
   * @return true, if the value is in the set
   */
  boolean inSet(String key, String value);

  /**
   * Create/Update a key/value pair.
   *
   * @param key key
   * @param value value
   * @param expirySeconds seconds to expiry
   */
  void put(String key, String value, long expirySeconds);

  /**
   * Create/Update a key/value pair on implementations that do not support TTL.
   *
   * @param key key
   * @param value value
   */
  void put(String key, String value);

  /**
   * Add value to a set.
   *
   * @param key key
   * @param value value
   * @param expirySeconds seconds to expiry
   */
  void putSet(String key, String value, long expirySeconds);

  /**
   * Add value to a set on implementations that do not support TTL.
   *
   * @param key key
   * @param value value
   */
  void putSet(String key, String value);

  /**
   * Set key/value pair only if the key does not exist.
   *
   * @param key key
   * @param value value
   * @param expirySeconds expiry seconds
   * @return true, if the value was set
   */
  boolean putIfNotExist(String key, String value, long expirySeconds);

  /**
   * Set key/value pair only if the key does not exist on implementations that do not support TTL.
   *
   * @param key key
   * @param value value
   */
  boolean putIfNotExist(String key, String value);

  /**
   * @return OK if the store is available
   */
  default String status() {
    return "OK";
  }

}
