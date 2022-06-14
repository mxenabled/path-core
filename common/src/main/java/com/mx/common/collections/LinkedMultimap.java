package com.mx.common.collections;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class LinkedMultimap<K, V> implements Multimap<K, V> {
  private final Map<K, List<V>> delegate;

  public LinkedMultimap() {
    this.delegate = new LinkedHashMap<>();
  }

  @Override
  public V getFirst(K key) {
    List<V> values = this.delegate.get(key);
    if (values != null && !values.isEmpty()) {
      return values.get(0);
    }
    return null;
  }

  @Override
  public void add(K key, V value) {
    List<V> values = this.delegate.computeIfAbsent(key, k -> new LinkedList<>());
    values.add(value);
  }

  @Override
  public void addAll(K key, List<? extends V> values) {
    List<V> currentValues = this.delegate.computeIfAbsent(key, k -> new LinkedList<>());
    currentValues.addAll(values);
  }

  @Override
  public void set(K key, V value) {
    List<V> values = new LinkedList<>();
    values.add(value);
    this.delegate.put(key, values);
  }

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

  @Override
  public int size() {
    return this.delegate.size();
  }

  @Override
  public boolean isEmpty() {
    return this.delegate.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return this.delegate.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return this.delegate.containsValue(value);
  }

  @Override
  public List<V> get(Object key) {
    return this.delegate.get(key);
  }

  @Override
  public List<V> put(K key, List<V> value) {
    return this.delegate.put(key, value);
  }

  @Override
  public List<V> remove(Object key) {
    return this.delegate.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends List<V>> map) {
    this.delegate.putAll(map);
  }

  @Override
  public void clear() {
    this.delegate.clear();
  }

  @Override
  public Set<K> keySet() {
    return this.delegate.keySet();
  }

  @Override
  public Collection<List<V>> values() {
    return this.delegate.values();
  }

  @Override
  public Set<Map.Entry<K, List<V>>> entrySet() {
    return this.delegate.entrySet();
  }

  @Override
  public boolean equals(Object other) {
    return this == other || this.delegate.equals(other);
  }

  @Override
  public int hashCode() {
    return this.delegate.hashCode();
  }

  @Override
  public String toString() {
    return this.delegate.toString();
  }
}
