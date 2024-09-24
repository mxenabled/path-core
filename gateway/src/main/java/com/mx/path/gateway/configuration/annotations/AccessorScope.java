package com.mx.path.gateway.configuration.annotations;

import java.util.Objects;

/**
 * Enum representing the scope of an accessor instance.
 */
public enum AccessorScope {

  SINGLETON(3, "singleton"),
  //  REQUEST(2, "request"), This is not yet supported. Adding here as a placeholder
  PROTOTYPE(1, "prototype");

  private final String name;
  private final int value;

  /**
   * Build new {@link AccessorScope} enum with given value and name.
   *
   * @param value numeric value associated with scope
   * @param name name of scope
   */
  AccessorScope(int value, String name) {
    this.value = value;
    this.name = name;
  }

  /**
   * Return accessor scope name.
   *
   * @return accessor scope name.
   */
  public final String getName() {
    return name;
  }

  /**
   * Return accessor scope value.
   *
   * @return accessor scope value.
   */
  public final int getValue() {
    return value;
  }

  /**
   * Find accessor scope by value.
   *
   * @param value value of accessor scope to be found
   * @return matching accessor scope, null if not found
   */
  public static AccessorScope resolve(int value) {
    for (AccessorScope a : values()) {
      if (a.value == value) {
        return a;
      }
    }
    return null;
  }

  /**
   * Find accessor scope by name.
   *
   * @param name name of accessor scope to be found
   * @return matching accessor scope, null if not found
   */
  public static AccessorScope resolve(String name) {
    for (AccessorScope a : values()) {
      if (Objects.equals(name, a.name)) {
        return a;
      }
    }
    return null;
  }
}
