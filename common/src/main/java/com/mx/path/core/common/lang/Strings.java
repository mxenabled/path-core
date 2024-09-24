package com.mx.path.core.common.lang;

/**
 * Utility class for string.
 */
public class Strings {

  /**
   * Check if string is blank.
   *
   * @param s string to check
   * @return true if string is blank
   */
  public static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }

  /**
   * Negation of {@link #isBlank(String)}.
   *
   * @param s string to check
   * @return true if string NOT blank
   */
  public static boolean isNotBlank(String s) {
    return !isBlank(s);
  }

}
