package com.mx.path.core.common.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Constructors {

  /**
   * Get no-argument constructor for given class
   * @param klass
   * @return no argument constructor if defined, other wise null.
   */
  @SuppressWarnings("unchecked")
  public static <T> Constructor<T> getNoArgumentConstructor(Class<T> klass) {
    Constructor<?>[] constructors = klass.getDeclaredConstructors();
    return (Constructor<T>) Arrays.stream(constructors)
        .filter(constructor -> constructor.getParameterCount() == 0)
        .findFirst()
        .orElse(null);
  }

  /**
   * Create instance of given class using no-argument constructor
   * @param klass
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T instantiateWithNoArgumentConstructor(Class<T> klass) {
    Constructor<?> constructor = getNoArgumentConstructor(klass);
    if (constructor == null) {
      throw new RuntimeException("No default constructor exists for " + klass);
    }

    boolean originalAccessibility = constructor.isAccessible();
    try {
      constructor.setAccessible(true);
      try {
        return (T) constructor.newInstance();
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException("Error constructing " + klass);
      }
    } finally {
      constructor.setAccessible(originalAccessibility);
    }
  }

  /**
   * Gets constructor with matching arg types. Returns null if not found.
   * @param klass
   * @param argClasses
   * @return
   * @param <T>
   */
  public static <T> Constructor<T> safeGetConstructor(Class<T> klass, Class<?>... argClasses) {
    try {
      return klass.getConstructor(argClasses);
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  /**
   * Create instance using constructor. Returns null if construction fails.
   * @param constructor
   * @param args
   * @return
   * @param <T>
   */
  public static <T> T safeInstantiate(Constructor<T> constructor, Object... args) {
    if (constructor == null) {
      return null;
    }

    try {
      return constructor.newInstance(args);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
      return null;
    }
  }
}
