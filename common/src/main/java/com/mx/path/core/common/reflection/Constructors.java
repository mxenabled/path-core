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

}
