package com.mx.path.core.common.collection;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Interface for maps that are backed by a multi-value map, {@code Map<String, List<String>>}
 * Provides a method to retrieve the raw underlying collection.
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public interface MultiValueMappable<K, V> {
  /**
   * Get entrySet that includes all values. If there are more than one values assigned to a key, then the key will
   * show up multiple times.
   *
   * @return flattened entrySet
   */
  default Set<Map.Entry<K, V>> flatEntrySet() {
    Set<Map.Entry<K, V>> entrySet = new LinkedHashSet<>();
    getRawMap().forEach((key, values) -> values.forEach(value -> {
      entrySet.add(new AbstractMap.SimpleEntry<>(key, value));
    }));

    return entrySet;
  }

  /**
   * Collects all values for all keys into a flat {@link List<V>}
   * @return a flat List of all values
   */
  default List<V> flatValues() {
    return getRawMap().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
  }

  /**
   * @return reference to underlying multi-value map
   */
  Map<K, List<V>> getRawMap();

  void setRawMap(Map<K, List<V>> rawMap);
}
