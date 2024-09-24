package com.mx.path.core.common.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface for exception reporter.
 */
public interface ExceptionReporter {

  /**
   * Report with parameters.
   *
   * @param ex exception
   * @param message message
   * @param context context
   */
  void report(@Nonnull Throwable ex, @Nullable String message, @Nonnull ExceptionContext context);
}
