package com.mx.path.api;

/**
 * Convenience class for wrapping strings in ANSI color codes. Useful for making error messages more readable.
 */
public final class AnsiWrapper {
  private static final String CYAN = "\u001B[36m";
  private static final String GREEN = "\u001B[32m";
  private static final String NO_COLOR = "\u001B[0m";
  private static final String RED = "\u001B[31m";
  private static final String YELLOW = "\u001B[33m";

  /**
   * Wraps the given text with cyan color codes.
   *
   * @param text text to wrap
   * @return text wrapped with cyan color codes
   */
  public String cyan(String text) {
    return CYAN + text + NO_COLOR;
  }

  /**
   * Wraps the given text with red color codes.
   *
   * @param text text to wrap
   * @return text wrapped with red color codes
   */
  public String red(String text) {
    return RED + text + NO_COLOR;
  }

  /**
   * Wraps the given text with green color codes.
   *
   * @param text text to wrap
   * @return text wrapped with green color codes
   */
  public String green(String text) {
    return GREEN + text + NO_COLOR;
  }

  /**
   * Wraps the given text with yellow color codes.
   *
   * @param text text to wrap
   * @return text wrapped with yellow color codes
   */
  public String yellow(String text) {
    return YELLOW + text + NO_COLOR;
  }
}
