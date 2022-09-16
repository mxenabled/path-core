package com.mx.path.gateway.process;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import com.mx.common.exception.GatewayException;

/**
 * This class can be used to easily spin off an asynchronous future that handle Gateway context propagation. By default,
 * this class will also handle submitting the future to a default executor service and handle catching & translating
 * thread exceptions into Gateway exceptions. This class is backed by the AsyncWithGatewayContext class.
 *
 * Note: the future begins executing immediately after object creation. This means you can spin off N async tasks that
 *       will run in parallel until you want to wait for them to finish.
 *
 * Usage:
 * // begins the API call immediately (takes ~2 seconds)
 * FutureWithGatewayContext<RemoteStrings> remoteStrings = new FutureWithGatewayContext<>(() -> api.getRemoteStrings());
 * // begins the API call immediately (takes ~2 seconds)
 * FutureWithGatewayContext<RemoteInt> remoteInt = new FutureWithGatewayContext<>(() -> api.getRemoteInt());
 *
 * RemoteStrings strings = remoteStrings.get(); // blocks
 * RemoteInt int = remoteInt.get(); // blocks
 *
 * Total request time: ~2 seconds instead of ~4.
 *
 * @param <T>
 */
public final class FutureWithGatewayContext<T> {
  private static final long DEFAULT_RETRIEVAL_TIMEOUT_MILLIS = 10000;
  private static final int DEFAULT_THREAD_POOL_SIZE = 20;
  private static final ExecutorService DEFAULT_EXECUTOR = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
  private long timeoutMillis = DEFAULT_RETRIEVAL_TIMEOUT_MILLIS;
  private final Future<T> future;

  public FutureWithGatewayContext(Supplier<T> lambda) {
    future = DEFAULT_EXECUTOR.submit(new AsyncWithGatewayContext<>(lambda));
  }

  public FutureWithGatewayContext(Supplier<T> lambda, long timeoutMillis) {
    this.future = DEFAULT_EXECUTOR.submit(new AsyncWithGatewayContext<>(lambda));
    this.timeoutMillis = timeoutMillis;
  }

  public FutureWithGatewayContext(Supplier<T> lambda, ExecutorService executorService) {
    this.future = executorService.submit(new AsyncWithGatewayContext<>(lambda));
  }

  public FutureWithGatewayContext(Supplier<T> lambda, ExecutorService executorService, long timeoutMillis) {
    this.future = executorService.submit(new AsyncWithGatewayContext<>(lambda));
    this.timeoutMillis = timeoutMillis;
  }

  public T get() {
    try {
      return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
    } catch (TimeoutException e) {
      throw new GatewayException("FutureWithGatewayContext timeout out", e);
    } catch (InterruptedException e) {
      throw new GatewayException("FutureWithGatewayContext was interrupted", e);
    } catch (ExecutionException e) {
      throw new GatewayException("FutureWithGatewayContext execution failed", e);
    }
  }
}
