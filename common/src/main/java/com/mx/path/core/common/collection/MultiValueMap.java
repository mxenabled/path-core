package com.mx.path.core.common.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A multi-value backed collection that acts like a multi-value map. This can be used interchangeably
 * with other {@link MultiValueMappable} classes.
 *
 * <pre>
 * The copy constructor will copy the reference to the backing collection (does not copy the collection).
 * Example:
 * {@code
 *   MultiValueMap<String, String> multiValue = new MultiValueMap<String, String>();
 *   multiValue.add("key1", "value1");
 *   multiValue.get("key1"); // => [ "value1" ]
 *
 *   SingleValueMap<String, String> singleValue = new SingleValueMap<>(multiValue);
 *   singleValue.get("key1"); // => "value1"
 * }
 * </pre>
 *
 * @param <K> key type
 * @param <V> value type
 */
public class MultiValueMap<K, V> implements Map<K, List<V>>, MultiValueMappable<K, V> {

  // Static methods

  /**
   * Convenience method for constructing a MultiValueMap for an existing Map.
   *
   * @param rawMap the raw map
   * @param <K> key type
   * @param <V> value type
   * @return new MultiValueMap with given raw map
   */
  public static <K, V> MultiValueMap<K, V> forMap(Map<K, List<V>> rawMap) {
    MultiValueMap<K, V> multiValueMap = new MultiValueMap<>();
    multiValueMap.setRawMap(rawMap);

    return multiValueMap;
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
   * Set raw map.
   *
   * @param rawMap the map
   */
  @Override
  public void setRawMap(Map<K, List<V>> rawMap) {
    this.rawMap = rawMap;
  }

  // Constructors

  /**
   * Default constructor.
   */
  public MultiValueMap() {
    this.rawMap = new LinkedHashMap<>();
  }

  /**
   * Copy constructor to move raw map from any MultiValueMappable object.
   *
   * @param multiValueMappable map to copy
   */
  public MultiValueMap(MultiValueMappable<K, V> multiValueMappable) {
    this.rawMap = multiValueMappable.getRawMap();
  }

  // Public Methods

  /**
   * Add the given single value to the current list of values for the given key.
   *
   * @param key the key
   * @param value the value to be added
   */
  public void add(K key, V value) {
    List<V> values = getRawMap().computeIfAbsent(key, k -> new LinkedList<>());
    values.add(value);
  }

  /**
   * Add all the values of the given list to the current list of values for the given key.
   *
   * @param key they key
   * @param values the values to be added
   */
  public void addAll(K key, List<? extends V> values) {
    List<V> currentValues = getRawMap().computeIfAbsent(key, k -> new LinkedList<>());
    currentValues.addAll(values);
  }

  /**
   * Add the given value, only when the map does not
   * contain the given key.
   *
   * @param key the key
   * @param value the value to be added
   */
  public void addIfAbsent(K key, V value) {
    if (!containsKey(key)) {
      add(key, value);
    }
  }

  /**
   * Clear all keys and values.
   */
  @Override
  public void clear() {
    getRawMap().clear();
  }

  /**
   * True if the key exists. Considered true even if the key's list of values is empty.
   *
   * @param key key to check
   * @return true if key exists
   */
  @Override
  public boolean containsKey(Object key) {
    return getRawMap().containsKey(key);
  }

  /**
   * True if
   * <ol>
   *   <li>{@code value} is a {@link List} and {@code value.equals()} any key's list</li>
   *   <li>{@code value} is a {@link V} and the value exists in any keys' list</li>
   * </ol>
   *
   * @param value the value
   * @return true, if found
   */
  @Override
  public boolean containsValue(Object value) {
    if (getRawMap().containsValue(value)) {
      return true;
    }

    return flatValues().contains(value);
  }

  /**
   * @return rawMap's entrySet
   */
  @Override
  public Set<Entry<K, List<V>>> entrySet() {
    return getRawMap().entrySet();
  }

  /**
   * Compares this and other
   * @param other other to compare
   * @return true, if equal
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
   * Get value list for key.
   *
   * @param key key
   * @return value list
   */
  @Override
  public List<V> get(Object key) {
    return getRawMap().get(key);
  }

  /**
   * Return the first value for the given key.
   *
   * @param key the key
   * @return the first value for the specified key, or null if none
   */
  public V getFirst(K key) {
    List<V> values = getRawMap().get(key);
    if (values != null && !values.isEmpty()) {
      return values.get(0);
    }
    return null;
  }

  /**
   * @return hashCode of rawMap
   */
  @Override
  public int hashCode() {
    return getRawMap().hashCode();
  }

  /**
   * @return isEmpty of rawMap. true, if no keys. Does not consider size of value lists.
   */
  @Override
  public boolean isEmpty() {
    return getRawMap().isEmpty();
  }

  /**
   * @return keySet of rawMap
   */
  @Override
  public Set<K> keySet() {
    return getRawMap().keySet();
  }

  /**
   * Set of overwrite value list for key.
   *
   * @param key key
   * @param value value to set
   * @return updated list
   */
  @Override
  public List<V> put(K key, List<V> value) {
    return getRawMap().put(key, value);
  }

  /**
   * Add all given key, value lists. Overwrites existing.
   *
   * @param map map to add
   */
  @Override
  public void putAll(Map<? extends K, ? extends List<V>> map) {
    getRawMap().putAll(map);
  }

  /**
   * Remove values for key.
   *
   * @param key the key
   * @return value list of removed key
   */
  @Override
  public List<V> remove(Object key) {
    return getRawMap().remove(key);
  }

  /**
   * Set the given single value under the given key. Overwrites existing value list.
   *
   * @param key the key
   * @param value the value to set
   */
  public void set(K key, V value) {
    List<V> values = new LinkedList<>();
    values.add(value);
    getRawMap().put(key, values);
  }

  /**
   * @return the size of the keySet. Does not count values.
   */
  @Override
  public int size() {
    return getRawMap().size();
  }

  /**
   * Create a new MultiValueMap containing only keys matching the provided {@link Pattern}.
   *
   * @param pattern {@link Pattern}
   * @return new MultiValueMap
   */
  public MultiValueMap<K, V> slice(Pattern pattern) {
    return slice(Collections.singletonList(pattern));
  }

  /**
   * Create a new MultiValueMap containing only keys matching at least one provided {@link Pattern}.
   *
   * @param keyPatterns list of {@link Pattern}
   * @return new MultiValueMap
   */
  public MultiValueMap<K, V> slice(Collection<Pattern> keyPatterns) {
    MultiValueMap<K, V> result = new MultiValueMap<>();

    rawMap.keySet().stream().filter((key) -> keyPatterns.stream().anyMatch((p) -> p.asPredicate().test(key.toString()))).forEach((key) -> {
      result.put(key, get(key));
    });

    return result;
  }

  /**
   * Convert to a {@link SingleValueMap}
   * Implementation Note: It is expected that multi-values are maintained in the
   * SingleMap as long as single map operations are performed on an existing key
   * values.
   *
   * @return a single value representation of this map
   */
  public SingleValueMap<K, V> toSingleValueMap() {
    return new SingleValueMap<>(this);
  }

  /**
   * @return toString of rawMap
   */
  @Override
  public String toString() {
    return getRawMap().toString();
  }

  /**
   * @return values of rawMap
   */
  @Override
  public Collection<List<V>> values() {
    return getRawMap().values();
  }
}
