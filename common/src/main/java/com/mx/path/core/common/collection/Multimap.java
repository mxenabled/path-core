package com.mx.path.core.common.collection;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Interface for multimap.
 *
 * @param <K> key type
 * @param <V> value type
 */
public interface Multimap<K, V> extends Map<K, List<V>>, Serializable {

  /**
   * Return the first value for the given key.
   *
   * @param key the key
   * @return the first value for the specified key, or null if none
   */
  V getFirst(K key);

  /**
   * Add the given single value to the current list of values for the given key.
   *
   * @param key the key
   * @param value the value to be added
   */
  void add(K key, V value);

  /**
   * Add all the values of the given list to the current list of values for the given key.
   *
   * @param key they key
   * @param values the values to be added
   */
  void addAll(K key, List<? extends V> values);

  /**
   * Add the given value, only when the map does not
   * contain the given key.
   *
   * @param key the key
   * @param value the value to be added
   */
  default void addIfAbsent(K key, V value) {
    if (!containsKey(key)) {
      add(key, value);
    }
  }

  /**
   * Set the given single value under the given key.
   *
   * @param key the key
   * @param value the value to set
   */
  void set(K key, V value);

  /**
   * Return a Map with the first values contained in this Multimap.
   * @return a single value representation of this map
   */
  Map<K, V> toSingleValueMap();

}
