package com.mx.path.testing.util;

import org.assertj.core.api.AssertionsForClassTypes;

/**
 * Utility class providing helper methods for unit testing.
 */
public class TestUtils {

  /**
   * Performs a deep, reflection-based comparison between two objects.
   * <p>
   * This method serves as a utility assertion; if the objects are not deeply equal or if their classes
   * do not exactly match, it will throw an {@link AssertionError} providing detailed information about the failure.
   * </p>
   *
   * @param obj1 the first object to compare
   * @param obj2 the second object to compare
   * @throws AssertionError if the objects are not deeply equal, not the same class, or if either object is null.
   */
  public static void deepEquals(Object obj1, Object obj2) {
    AssertionsForClassTypes.assertThat(obj2)
        .isNotNull();
    AssertionsForClassTypes.assertThat(obj1)
        .isNotNull()
        .hasSameClassAs(obj2)
        .usingRecursiveComparison()
        .isEqualTo(obj2);
  }
}
