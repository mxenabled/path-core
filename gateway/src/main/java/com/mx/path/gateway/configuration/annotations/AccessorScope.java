package com.mx.path.gateway.configuration.annotations;

import java.util.Objects;

public enum AccessorScope {

  SINGLETON(3, "singleton"),
  //  REQUEST(2, "request"), This is not yet supported. Adding here as a placeholder
  PROTOTYPE(1, "prototype");

  private final String name;
  private final int value;

  AccessorScope(int value, String name) {
    this.value = value;
    this.name = name;
  }

  public final String getName() {
    return name;
  }

  public final int getValue() {
    return value;
  }

  public static AccessorScope resolve(int value) {
    for (AccessorScope a : values()) {
      if (a.value == value) {
        return a;
      }
    }
    return null;
  }

  public static AccessorScope resolve(String name) {
    for (AccessorScope a : values()) {
      if (Objects.equals(name, a.name)) {
        return a;
      }
    }
    return null;
  }
}
