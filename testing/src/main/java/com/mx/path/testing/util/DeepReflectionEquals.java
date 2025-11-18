package com.mx.path.testing.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.argThat;

import org.mockito.ArgumentMatcher;

/**
 * A Mockito {@link ArgumentMatcher} that performs a deep reflection-based equality check
 * using AssertJ's recursive comparison.
 * <p>
 * This matcher is particularly useful for verifying method arguments when the object being passed
 * does not implement {@link Object#equals(Object)}, or when you need to exclude specific
 * fields (such as timestamps, auto-generated IDs, or random tokens) from the comparison.
 * </p>
 *
 * <b>Usage Example:</b>
 * <pre>
 * // Verify that save was called with a specific object, ignoring the "id" field
 * verify(myRepository).save(deepRefEq(expectedEntity, "id"));
 * </pre>
 *
 * @param <T> the type of the object being matched
 */
public class DeepReflectionEquals<T> implements ArgumentMatcher<T> {

  private final T expected;
  private final String[] excludedFields;

  /**
   * Constructs a new deep reflection matcher.
   *
   * @param expected      the expected object instance to compare against
   * @param excludeFields an optional array of field names to ignore during the recursive comparison
   */
  public DeepReflectionEquals(T expected, String... excludeFields) {
    this.expected = expected;
    this.excludedFields = excludeFields;
  }

  /**
   * Checks if the actual argument matches the expected object using deep recursive comparison.
   *
   * @param actual the actual argument passed to the mock
   * @return {@code true} if the objects are deeply equal (ignoring excluded fields); {@code false} otherwise
   */
  @Override
  public boolean matches(Object actual) {
    try {
      assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields(excludedFields)
          .isEqualTo(expected);
      return true;
    } catch (Throwable e) {
      return false;
    }
  }

  /**
   * A static factory method to create a deep reflection matcher.
   * <p>
   * This is the primary entry point for using this matcher within Mockito's {@code verify}
   * or {@code when} clauses.
   * </p>
   *
   * @param value         the expected value to match against
   * @param excludeFields (Optional) a list of field names to exclude from the comparison
   * @param <T>           the type of the object
   * @return a dummy value of type {@code T} (internally provided by Mockito)
   */
  public static <T> T deepRefEq(T value, String... excludeFields) {
    return argThat(new DeepReflectionEquals<>(value, excludeFields));
  }
}
