package com.mx.path.core.common.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A multi-value backed collection that acts like a single-value map. This can be used interchangeably with other {@link MultiValueMappable} classes.
 *
 * <p>The copy constructor will copy the reference to the backing collection (does not copy the collection).
 *
 * <p>Example:
 * <pre>{@code
 *   SingleValueMap<String, String> singleValue = new SingleValueMap<>();
 *   singleValue.put("key1", "value1");
 *   singleValue.get("key1"); // => "value1"
 *
 *   MultiValueMap<String, String> multiValue = new MultiValueMap<String, String>(singleValue);
 *   multiValue.get("key1"); // => [ "value1" ]
 * }</pre>
 *
 * <i>Multi-Value support:</i>
 * <p>This map will preserve key multi-values with all read operations. Write operations will replace the list of values with a new list.
 *
 * <p>If multi-value support is needed, use {@link MultiValueMap}.
 *
 * <p>Interface for map of single values
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class SingleValueMap<K, V> implements Map<K, V>, MultiValueMappable<K, V> {

  // Static Methods

  /**
   * Convenience method for constructing a SingleValueMap for an existing Map
   *
   * @param rawMap the raw map
   * @param <K> key type
   * @param <V> value type
   * @return new SingleValueMap with given raw map
   */
  public static <K, V> SingleValueMap<K, V> forMap(Map<K, List<V>> rawMap) {
    SingleValueMap<K, V> singleValueMap = new SingleValueMap<>();
    singleValueMap.setRawMap(rawMap);

    return singleValueMap;
  }

  // Fields

  private Map<K, List<V>> rawMap;

  // Getters/Setters

  /**
   * @return reference to map backing the MultiValue map
   */
  @Override
  public Map<K, List<V>> getRawMap() {
    return rawMap;
  }

  /**
   * Set raw map
   *
   * @param rawMap the map
   */
  @Override
  public void setRawMap(Map<K, List<V>> rawMap) {
    this.rawMap = rawMap;
  }

  // Constructors

  public SingleValueMap() {
    this.rawMap = new LinkedHashMap<>();
  }

  /**
   * Copy constructor to move raw map from any MultiValueMappable object.
   *
   * @param multiValueMap
   */
  public SingleValueMap(MultiValueMappable<K, V> multiValueMap) {
    this.rawMap = multiValueMap.getRawMap();
  }

  // Public Methods

  /**
   * Clear all keys and values
   */
  @Override
  public void clear() {
    getRawMap().clear();
  }

  /**
   * @param key the key
   * @return true, if the key exists
   */
  @Override
  public boolean containsKey(Object key) {
    return getRawMap().containsKey(key);
  }

  /**
   * @param value the value
   * @return true, if the value is first value for any key
   */
  @Override
  public boolean containsValue(Object value) {
    return values().contains(value);
  }

  /**
   * @return entrySet of key and first values
   */
  @Override
  public Set<Entry<K, V>> entrySet() {
    return toMap().entrySet();
  }

  /**
   * Compares this with other. Will compare the rawMap's if other is MultiValueMappable
   *
   * @param other
   * @return true if they are equal
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }

    if (this == other) {
      return true;
    }

    if (MultiValueMappable.class.isAssignableFrom(other.getClass())) {
      return this.rawMap.equals(((MultiValueMappable) other).getRawMap());
    }

    return getRawMap().equals(other);
  }

  /**
   * @param key the key
   * @return the first value of key
   */
  @Override
  @SuppressWarnings("unchecked")
  public V get(Object key) {
    if (key == null) {
      return null;
    }

    return getFirst((K) key);
  }

  /**
   * hashCode of rawMap
   *
   * @return
   */
  @Override
  public int hashCode() {
    return getRawMap().hashCode();
  }

  /**
   * isEmpty of rawMap
   *
   * @return
   */
  @Override
  public boolean isEmpty() {
    return getRawMap().isEmpty();
  }

  /**
   * keySet of rawMap
   *
   * @return
   */
  @Override
  public Set<K> keySet() {
    return getRawMap().keySet();
  }

  /**
   * Sets first value for key.
   *
   * <p><i>Multi-value Support:</i>
   * <p>This operation will remove all other values associated with key. If multi-value support is needed, convert this collection to a {@link MultiValueMap}
   *
   * @param key the key
   * @param value the value
   * @return
   */
  @Override
  public V put(K key, V value) {
    return setFirst(key, value);
  }

  /**
   * Sets first value for all keys given.
   *
   * <p><i>Multi-value Support:</i>
   * <p>This operation will remove all other values associated with provided keys. If multi-value support is needed, convert this collection to a {@link MultiValueMap}
   *
   * @param map Map of key-values
   */
  @Override
  public void putAll(Map<? extends K, ? extends V> map) {
    map.forEach(this::setFirst);
  }

  /**
   * Remove key
   *
   * <p><i>Multi-value Support:</i>
   * <p>This operation will remove all other values associated with provided key. If multi-value support is needed, convert this collection to a {@link MultiValueMap}
   *
   * @param key the key
   * @return the first value associated with the removed key.
   */
  @Override
  @SuppressWarnings("unchecked")
  public V remove(Object key) {
    V value = getFirst((K) key);
    getRawMap().remove(key);
    return value;
  }

  /**
   * @return size of rawMap
   */
  @Override
  public int size() {
    return getRawMap().size();
  }

  /**
   * Convert to regular Map
   *
   * @return map of key first-value pairs
   */
  public Map<K, V> toMap() {
    Map<K, V> map = new LinkedHashMap<>();
    for (Entry<K, List<V>> entry : getRawMap().entrySet()) {
      List<V> values = entry.getValue();
      if (values != null && !values.isEmpty()) {
        map.put(entry.getKey(), values.get(0));
      } else {
        map.put(entry.getKey(), null);
      }
    }
    return map;
  }

  /**
   * @return Convert this to a {@link MultiValueMap}
   */
  public MultiValueMap<K, V> toMultiValueMap() {
    return new MultiValueMap<>(this);
  }

  /**
   * @return toString of rawMap
   */
  @Override
  public String toString() {
    return getRawMap().toString();
  }

  /**
   * Collection of first values to all keys.
   *
   * <p><i>Multi-value Support:</i>
   * <p>This operation will remove all other values associated with provided key. If multi-value support is needed, convert this collection to a {@link MultiValueMap}
   *
   * @return first values for all keys
   */
  @Override
  public Collection<V> values() {
    return toMap().values();
  }

  // Private Methods

  private V getFirst(K key) {
    List<V> values = getRawMap().get(key);
    if (values != null && !values.isEmpty()) {
      return values.get(0);
    }
    return null;
  }

  private V setFirst(K key, V value) {
    List<V> values = getRawMap().computeIfAbsent(key, k -> new ArrayList<>(1));
    values.clear();
    values.add(value);

    return value;
  }
}
