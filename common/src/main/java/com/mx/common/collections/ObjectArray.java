package com.mx.common.collections;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * Free-form object array.
 *
 * Has safe {@link #get(int)} that returns null if out of
 * bounds as well as utility methods for dealing with ObjectMap
 */
public class ObjectArray extends ArrayList<Object> {

  /**
   * Creates and adds an ObjectArray
   * @return the new ObjectArray
   */
  public final ObjectArray createArray() {
    ObjectArray array = new ObjectArray();
    add(array);

    return array;
  }

  /**
   * Creates and adds an ObjectMap
   * @return the new ObjectMap
   */
  public final ObjectMap createMap() {
    ObjectMap map = new ObjectMap();
    add(map);

    return map;
  }

  /**
   * @param t type
   * @param index of item
   * @param <T> type
   * @return index as given type
   */
  @SuppressWarnings("unchecked")
  public final <T> T getAs(Class<T> t, int index) {
    return (T) get(index);
  }

  /**
   * @param t type
   * @param index of item
   * @param <T> type
   * @param def default
   * @return index as given type, default if index is null
   */
  @SuppressWarnings("unchecked")
  public final <T> T getAs(Class<T> t, int index, T def) {
    if (isNotNull(index)) {
      return (T) get(index);
    }

    return def;
  }

  /**
   * Get index as an ObjectArray
   * @param index of item
   * @return ObjectArray at index or null
   */
  public final ObjectArray getArray(int index) {
    if (isArray(index)) {
      return (ObjectArray) get(index);
    }

    return null;
  }

  /**
   * Get index as a Boolean
   *
   * <p>Does a flexible conversion to boolean.
   * True values are {@code true, "true", "True", "1", 1, 1.0 }
   * <p>All other values result in false.
   *
   * @param index of item
   * @return Null if not a value, True if index is a true value, false if a non-true value.
   */
  @Nullable
  public final Boolean getAsBoolean(int index) {
    if (isValue(index)) {
      Object value = get(index);

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
      return ObjectMap.TRUE_VALUES.stream().anyMatch(trueValue -> Objects.equals(trueValue, valueToTest));
    }

    return null;
  }

  /**
   * Get key as a Boolean
   *
   * <p>Does a flexible conversion to boolean.
   * True values are {@code true, "true", "True", "1", 1, 1.0 }
   * <p>All other values result in false.
   *
   * @param key of item
   * @param def Default value
   * @return Default if not a value, True if key is a true value, false if a non-true value.
   */
  public final boolean getAsBoolean(int key, boolean def) {
    Boolean value = getAsBoolean(key);

    if (value == null) {
      return def;
    }

    return value;
  }

  /**
   * @param index of item
   * @return Integer value of index or null
   */
  public final Integer getAsInteger(int index) {
    if (isValue(index)) {
      Object value = get(index);

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
   * @param index of item
   * @param def default value
   * @return Integer value of index or def
   */
  public final Integer getAsInteger(int index, int def) {
    Integer value = getAsInteger(index);

    if (value == null) {
      return def;
    }

    return value;
  }

  /**
   * @param index of item
   * @return Long value of index or null
   */
  public final Long getAsLong(int index) {
    if (isValue(index)) {
      Object value = get(index);

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
   * @param index of item
   * @param def default value
   * @return Long value of index or def
   */
  public final Long getAsLong(int index, long def) {
    Long value = getAsLong(index);

    if (value == null) {
      return def;
    }

    return value;
  }

  /**
   * @param index of item
   * @return String value of index
   */
  public final String getAsString(int index) {
    if (isValue(index)) {
      return String.valueOf(get(index));
    }

    return null;
  }

  /**
   * @param index of item
   * @param def default value
   * @return String value of index or def
   */
  public final String getAsString(int index, String def) {
    String value = getAsString(index);

    if (value == null) {
      return def;
    }

    return value;
  }

  /**
   * Get index as an ObjectMap
   * @param index of item
   * @return ObjectMap at index or null
   */
  public final ObjectMap getMap(int index) {
    if (isMap(index)) {
      return (ObjectMap) get(index);
    }

    return null;
  }

  /**
   * @param index of item
   * @return true if index is an ObjectArray
   */
  public final boolean isArray(int index) {
    if (isNotNull(index) && ObjectArray.class.isAssignableFrom(get(index).getClass())) {
      return true;
    }

    return false;
  }

  /**
   * @param index of item
   * @return true if index is an ObjectMap
   */
  public final boolean isMap(int index) {
    if (isNotNull(index) && ObjectMap.class.isAssignableFrom(get(index).getClass())) {
      return true;
    }

    return false;
  }

  /**
   * @param index of item
   * @return true if index is a value
   */
  public final boolean isValue(int index) {
    if (isNotNull(index)) {
      Class<?> valueClass = get(index).getClass();
      return valueClass == String.class
          || valueClass == Boolean.class
          || valueClass == Integer.class
          || valueClass == Long.class
          || valueClass == BigDecimal.class
          || valueClass == Float.class;
    }

    return false;
  }

  /**
   * @param index of item
   * @return true if index is not null
   */
  public final boolean isNotNull(int index) {
    return !isNull(index);
  }

  /**
   * @param index of item
   * @return true if index is a null
   */
  public final boolean isNull(int index) {
    if (get(index) == null) {
      return true;
    }

    return false;
  }

  // List Overrides

  /**
   * Safe get. Returns null if out of bounds.
   *
   * @param index of item
   * @return object or null
   */
  @Override
  public final Object get(int index) {
    if (index >= size()) {
      return null;
    }

    return super.get(index);
  }
}
