package com.mx.common.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ExceptionReporter {
  void report(@Nonnull Throwable ex, @Nullable String message, @Nonnull ExceptionContext context);
}
