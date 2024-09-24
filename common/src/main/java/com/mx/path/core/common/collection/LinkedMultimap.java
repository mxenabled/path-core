package com.mx.path.core.common.collection;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Allow mapping of one key to multiple values.
 *
 * @param <K> type of key
 * @param <V> type of values
 */
public final class LinkedMultimap<K, V> implements Multimap<K, V> {
  private final Map<K, List<V>> delegate;

  /**
   * Default constructor.
   */
  public LinkedMultimap() {
    this.delegate = new LinkedHashMap<>();
  }

  /**
   * Get first value associated with key.
   *
   * @param key the key
   * @return first value
   */
  @Override
  public V getFirst(K key) {
    List<V> values = this.delegate.get(key);
    if (values != null && !values.isEmpty()) {
      return values.get(0);
    }
    return null;
  }

  /**
   * Add new value to key.
   *
   * @param key the key
   * @param value the value to be added
   */
  @Override
  public void add(K key, V value) {
    List<V> values = this.delegate.computeIfAbsent(key, k -> new LinkedList<>());
    values.add(value);
  }

  /**
   * Add multiple values to key.
   *
   * @param key they key
   * @param values the values to be added
   */
  @Override
  public void addAll(K key, List<? extends V> values) {
    List<V> currentValues = this.delegate.computeIfAbsent(key, k -> new LinkedList<>());
    currentValues.addAll(values);
  }

  /**
   * Override current key value or set new values.
   *
   * @param key the key
   * @param value the value to set
   */
  @Override
  public void set(K key, V value) {
    List<V> values = new LinkedList<>();
    values.add(value);
    this.delegate.put(key, values);
  }

  /**
   * Map to single value map were only the first value associated with each key is used.
   *
   * @return single value map
   */
  @Override
  public Map<K, V> toSingleValueMap() {
    Map<K, V> singleValueMap = new LinkedHashMap<>(this.delegate.size());
    this.delegate.forEach((key, values) -> {
      if (values != null && !values.isEmpty()) {
        singleValueMap.put(key, values.get(0));
      }
    });
    return singleValueMap;
  }

  /**
   * Map size.
   *
   * @return size
   */
  @Override
  public int size() {
    return this.delegate.size();
  }

  /**
   * Check if map is empty.
   *
   * @return true if empty
   */
  @Override
  public boolean isEmpty() {
    return this.delegate.isEmpty();
  }

  /**
   * Check if map has key.
   *
   * @param key key whose presence in this map is to be tested
   * @return true if map contains key
   */
  @Override
  public boolean containsKey(Object key) {
    return this.delegate.containsKey(key);
  }

  /**
   * Check if map contains value.
   *
   * @param value value whose presence in this map is to be tested
   * @return true if value on map
   */
  @Override
  public boolean containsValue(Object value) {
    return this.delegate.containsValue(value);
  }

  /**
   * Get list of values associated with given key.
   *
   * @param key the key whose associated value is to be returned
   * @return list of values
   */
  @Override
  public List<V> get(Object key) {
    return this.delegate.get(key);
  }

  /**
   * Add new value to key.
   *
   * @param key key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return list of values updated
   */
  @Override
  public List<V> put(K key, List<V> value) {
    return this.delegate.put(key, value);
  }

  /**
   * Remove value from key.
   *
   * @param key key whose mapping is to be removed from the map
   * @return list of values updated
   */
  @Override
  public List<V> remove(Object key) {
    return this.delegate.remove(key);
  }

  /**
   * Insert all key and values on map.
   *
   * @param map mappings to be stored in this map
   */
  @Override
  public void putAll(Map<? extends K, ? extends List<V>> map) {
    this.delegate.putAll(map);
  }

  /**
   * Clear map.
   */
  @Override
  public void clear() {
    this.delegate.clear();
  }

  /**
   * Return key set.
   *
   * @return key set
   */
  @Override
  public Set<K> keySet() {
    return this.delegate.keySet();
  }

  /**
   * Return list of values store on map.
   *
   * @return values
   */
  @Override
  public Collection<List<V>> values() {
    return this.delegate.values();
  }

  /**
   * Return map entry set.
   *
   * @return entry set
   */
  @Override
  public Set<Map.Entry<K, List<V>>> entrySet() {
    return this.delegate.entrySet();
  }

  /**
   * Check if maps is equal to object.
   *
   * @param other object to be compared for equality with this map
   * @return true if equal
   */
  @Override
  public boolean equals(Object other) {
    return this == other || this.delegate.equals(other);
  }

  /**
   * @return hashcode value of map
   */
  @Override
  public int hashCode() {
    return this.delegate.hashCode();
  }

  /**
   * @return string representation of map
   */
  @Override
  public String toString() {
    return this.delegate.toString();
  }
}
