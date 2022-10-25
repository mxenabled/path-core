package com.mx.path.gateway.process;

import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Wraps a lambda in a thread with Gateway context propagation. Allows you to run some code asynchronously without
 * having to create classes to capture the thread logic and thread result.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * AsyncWithGatewayContext<List<String>> getStringsThread = new AsyncWithGatewayContext<>(() -> api.getStrings());
 * Future<List<String>> stringsFuture = executorService.submit(getStringsThread);
 * List<String> strings = stringsFuture.get();
 * }</pre>
 *
 * @param <T> result type
 */
public final class AsyncWithGatewayContext<T> extends CallableWithGatewayContext<T> {
  @Getter(AccessLevel.PACKAGE)
  private final Supplier<T> lambda;

  public AsyncWithGatewayContext(Supplier<T> lambda) {
    this.lambda = lambda;
  }

  @Override
  protected T execute() throws Exception {
    return lambda.get();
  }
}
