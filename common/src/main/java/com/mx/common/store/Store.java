package com.mx.common.store;

import java.util.Set;

/**
 * Key-Value Store
 */
public interface Store {

  /**
   * Delete key
   * @param key
   */
  void delete(String key);

  /**
   * Delete a value from a set
   * @param key
   * @param value
   */
  void deleteSet(String key, String value);

  /**
   * Get value
   * @param key
   * @return
   */
  String get(String key);

  /**
   * Read a set
   * @param key
   * @return
   */
  Set<String> getSet(String key);

  /**
   * Query set for existence of a value
   * @param key
   * @param value
   * @return true, if the value is in the set
   */
  boolean inSet(String key, String value);

  /**
   * Create/Update a key/value pair
   * @param key
   * @param value
   * @param expirySeconds
   */
  void put(String key, String value, long expirySeconds);

  /**
   * Create/Update a key/value pair on implementations that do not support TTL
   * @param key
   * @param value
   */
  void put(String key, String value);

  /**
   * Add value to a set
   * @param key
   * @param value
   * @param expirySeconds
   */
  void putSet(String key, String value, long expirySeconds);

  /**
   * Add value to a set on implementations that do not support TTL
   * @param key
   * @param value
   */
  void putSet(String key, String value);

  /**
   * Set key/value pair only if the key does not exist
   * @param key
   * @param value
   * @param expirySeconds
   * @return true, if the value was set
   */
  boolean putIfNotExist(String key, String value, long expirySeconds);

  /**
   * Set key/value pair only if the key does not exist on implementations that do not support TTL
   * @param key
   * @param value
   */
  boolean putIfNotExist(String key, String value);

  /**
   * @return OK if the store is available
   */
  default String status() {
    return "OK";
  }

}
