package com.mx.path.core.common.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

/**
* Free-form object map.
*
* Note: Good for representing Json or Yaml objects in memory.
*/
public class ObjectMap extends LinkedHashMap<String, Object> {

  /**
   * List of reference values.
   */
  public static final List<Object> TRUE_VALUES;
  static {
    List<Object> newTrueList = new ArrayList<>();

    newTrueList.add(1);
    newTrueList.add((Long) 1L);
    newTrueList.add((Double) 1.0);
    newTrueList.add("1");
    newTrueList.add("true");

    TRUE_VALUES = Collections.unmodifiableList(newTrueList);
  }

  /**
   * Creates and adds an ObjectArray.
   *
   * @param key of item
   * @return the new ObjectArray
   */
  public final ObjectArray createArray(String key) {
    ObjectArray newArray = new ObjectArray();
    put(key, newArray);

    return newArray;
  }

  /**
   * Creates and adds an ObjectMap.
   *
   * @param key of item
   * @return the new ObjectMap
   */
  public final ObjectMap createMap(String key) {
    ObjectMap map = new ObjectMap();
    put(key, map);

    return map;
  }

  /**
   * Deep copy given into this.
   *
   * Maps are deep copied. Arrays are appended by reference. Other values are copied by reference.
   * @param m map to copy from
   * @return this
   */
  public final ObjectMap deepMerge(ObjectMap m) {
    m.forEach((k, v) -> {
      if (isMap(k) && v instanceof ObjectMap) {
        getMap(k).deepMerge((ObjectMap) v);
      } else if (isArray(k) && v instanceof ObjectArray) {
        getArray(k).addAll((ObjectArray) v);
      } else {
        put(k, v);
      }
    });

    return this;
  }

  /**
   * Get value as specified type.
   *
   * @param t type
   * @param key of item
   * @param <T> type
   * @return key as given type
   */
  @SuppressWarnings("unchecked")
  public final <T> T getAs(Class<T> t, String key) {
    return (T) get(key);
  }

  /**
   * Get value as specified type.
   *
   * @param t type
   * @param key of item
   * @param <T> type
   * @param def default
   * @return key as given type, default if key is null
   */
  @SuppressWarnings("unchecked")
  public final <T> T getAs(Class<T> t, String key, T def) {
    if (isNotNull(key)) {
      return (T) get(key);
    }

    return def;
  }

  /**
   * Get key as an ObjectArray.
   *
   * @param key of item
   * @return key as array
   */
  @SuppressWarnings("unchecked")
  public final ObjectArray getArray(String key) {
    if (isArray(key)) {
      return getAs(ObjectArray.class, key);
    }

    return null;
  }

  /**
   * Get key as a Boolean.
   *
   * <p>Does a flexible conversion to boolean.
   * True values are {@code true, "true", "True", "1", 1, 1.0 }
   * <p>All other values result in false.
   *
   * @param key of item
   * @return Null if not a value, True if key is a true value, false if a non-true value.
   */
  @Nullable
  public final Boolean getAsBoolean(String key) {
    if (isValue(key)) {
      Object value = get(key);

      if (value.getClass() == Boolean.class) {
        return (Boolean) value;
      }

      // Coerce the value into something that is comparable.
      if (value.getClass() == Double.class) {
        value = ((Double) value).intValue();
      }

      if (value.getClass() == BigDecimal.class) {
        value = ((BigDecimal) value).intValue();
      }

      final Object valueToTest = value;
      return TRUE_VALUES.stream().anyMatch(trueValue -> Objects.equals(trueValue, valueToTest));
    }

    return null;
  }

  /**
   * Get key as a Boolean.
   *
   * <p>Does a flexible conversion to boolean.
   * True values are {@code true, "true", "True", "1", 1, 1.0 }
   * <p>All other values result in false.
   *
   * @param key of item
   * @param def Default value
   * @return Default if not a value, True if key is a true value, false if a non-true value.
   */
  public final boolean getAsBoolean(String key, boolean def) {
    Boolean value = getAsBoolean(key);

    if (value == null) {
      return def;
    }

    return value;
  }

  /**
   * @param key of item
   * @return Integer value of key or null
   */
  public final Integer getAsInteger(String key) {
    if (isValue(key)) {
      Object value = get(key);

      if (value.getClass() == Integer.class || value.getClass() == Double.class) {
        return (Integer) value;
      }

      if (value.getClass() == Long.class) {
        return ((Long) value).intValue();
      }

      if (value.getClass() == BigDecimal.class) {
        return ((BigDecimal) value).intValue();
      }

      if (value.getClass() == String.class) {
        return Integer.parseInt((String) value);
      }
    }

    return null;
  }

  /**
   * @param key of item
   * @param def default value
   * @return Integer value of key or def
   */
  public final Integer getAsInteger(String key, int def) {
    Integer value = getAsInteger(key);

    if (value == null) {
      return def;
    }

    return value;
  }

  /**
   * @param key of item
   * @return Long value of key or null
   */
  public final Long getAsLong(String key) {
    if (isValue(key)) {
      Object value = get(key);

      if (value.getClass() == Long.class || value.getClass() == Double.class) {
        return (Long) value;
      }

      if (value.getClass() == Integer.class) {
        return ((Integer) value).longValue();
      }

      if (value.getClass() == BigDecimal.class) {
        return ((BigDecimal) value).longValue();
      }

      if (value.getClass() == String.class) {
        return Long.parseLong((String) value);
      }
    }

    return null;
  }

  /**
   * @param key of item
   * @param def default value
   * @return Long value of key or def
   */
  public final Long getAsLong(String key, long def) {
    Long value = getAsLong(key);

    if (value == null) {
      return def;
    }

    return value;
  }

  /**
   * @param key of item
   * @return String value of key
   */
  public final String getAsString(String key) {
    if (isValue(key)) {
      return String.valueOf(get(key));
    }

    return null;
  }

  /**
   * @param key of item
   * @param def default value
   * @return String value of key or def
   */
  public final String getAsString(String key, String def) {
    String value = getAsString(key);

    if (value == null) {
      return def;
    }

    return value;
  }

  /**
   * Get key as an ObjectMap.
   *
   * @param key of item
   * @return key as ObjectMap
   */
  public final ObjectMap getMap(String key) {
    if (isMap(key)) {
      return getAs(ObjectMap.class, key);
    }

    return null;
  }

  /**
   * @param key of item
   * @return true if key is an ObjectArray
   */
  public final boolean isArray(String key) {
    if (isNotNull(key)) {
      return ObjectArray.class.isAssignableFrom(get(key).getClass());
    }

    return false;
  }

  /**
   * @param key of item
   * @return true if key is an ObjectMap
   */
  public final boolean isMap(String key) {
    if (isNotNull(key)) {
      return ObjectMap.class.isAssignableFrom(get(key).getClass());
    }

    return false;
  }

  /**
   * @param key of item
   * @return true if key is a value type
   */
  public final boolean isValue(String key) {
    if (isNotNull(key)) {
      Class<?> valueClass = get(key).getClass();
      return valueClass == String.class
          || valueClass == Boolean.class
          || valueClass == Integer.class
          || valueClass == Long.class
          || valueClass == BigDecimal.class
          || valueClass == Float.class
          || valueClass == Double.class;
    }

    return false;
  }

  /**
   * @param key of item
   * @return true if key exists and is not null
   */
  public final boolean isNotNull(String key) {
    return !isNull(key);
  }

  /**
   * @param key of item
   * @return true if key does not exist or is null
   */
  public final boolean isNull(String key) {
    return !containsKey(key) || get(key) == null;
  }

}
