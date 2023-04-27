package com.mx.path.core.context.store;

import java.util.Objects;
import java.util.function.Supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.process.Lock;
import com.mx.path.core.common.store.Store;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Uses a {@link Store} to provide global mutex operations.
 *
 * Example:
 * <pre>{@code
 *   try (Lock mutex = new StoreLock(store, "account_cache", configurations)) {
 *     Lock.LockState state = mutex.request();
 *     if (state == Lock.LockState.NotAcquired) {
 *       state = mutex.acquireOr(() -> conditionMet());
 *       switch(state) {
 *         Lock.LockState.ConditionMet:
 *           // The condition was met
 *           break;
 *
 *         case Lock.LockState.Acquired:
 *           // Lock was acquired
 *           break;
 *
 *         case Lock.LockState.Timeout:
 *           // Timeout exceeded before lock acquired or condition met
 *           break;
 *       }
 *     }
 *   }
 * }</pre>
 *
 * NOT Thread-safe, NOT reusable. New instance required per request.
 */
public class StoreLock extends Lock {

  private static final int DEFAULT_TOKEN_LENGTH = 30;
  private static final long DEFAULT_POLL_MILLISECONDS = 50L;
  private static final int DEFAULT_MAX_LOCK_LENGTH_SECONDS = 10;
  private static final long DEFAULT_ACQUIRE_TIMEOUT_MILLISECONDS = 30L;

  // Fields

  @Getter
  private final String lockKey;
  @Getter
  @Setter
  private long acquireTimeoutMilliseconds;
  @Getter
  @Setter
  private long pollMilliseconds;
  @Getter
  @Setter
  private int maxLockLengthSeconds;
  private final Store store;
  @Getter
  private final String token;

  // Constructors

  /**
   * @param store implementation
   * @param key for resource to lock
   * @param configurations for mutex
   */
  public StoreLock(Store store, String key, ObjectMap configurations) {
    this.lockKey = "lock_token:" + key;
    this.token = RandomStringUtils.randomAlphanumeric(configurations.getAsInteger("tokenLength", DEFAULT_TOKEN_LENGTH));
    this.pollMilliseconds = configurations.getAsLong("pollMilliseconds", DEFAULT_POLL_MILLISECONDS);
    this.maxLockLengthSeconds = configurations.getAsInteger("maxLockLengthSeconds", DEFAULT_MAX_LOCK_LENGTH_SECONDS);
    this.acquireTimeoutMilliseconds = configurations.getAsLong("acquireTimeoutMilliseconds", DEFAULT_ACQUIRE_TIMEOUT_MILLISECONDS);
    this.store = store;
  }

  // Public

  public final String currentLockValue() {
    return store.get(lockKey);
  }

  // SessionLock

  @Override
  public final LockState acquire() {
    long start = System.currentTimeMillis();

    while (!acquired()) {
      if (acquireTimeoutMilliseconds > 0 && (System.currentTimeMillis() - start) >= acquireTimeoutMilliseconds) {
        return acquired() ? LockState.Acquired : LockState.Timeout;
      }

      request();

      try {
        Thread.sleep(pollMilliseconds);
      } catch (InterruptedException e) {
      }
    }

    return LockState.Acquired;
  }

  @Override
  public final boolean acquired() {
    return Objects.equals(token, currentLockValue());
  }

  @SneakyThrows
  @Override
  public final Lock.LockState acquireOr(Supplier<Boolean> waitUntil) {
    long start = System.currentTimeMillis();

    do {
      if (acquireTimeoutMilliseconds > 0 && (System.currentTimeMillis() - start) >= acquireTimeoutMilliseconds) {
        return acquired() ? LockState.Acquired : LockState.Timeout;
      }

      if (waitUntil.get()) {
        return LockState.ConditionMet;
      }

      request();

      if (acquired()) {
        return LockState.Acquired;
      }

      Thread.sleep(pollMilliseconds);
    } while (true);
  }

  @Override
  public final void close() {
    if (acquired()) {
      store.delete(lockKey);
    }
  }

  @Override
  public final LockState request() {
    return store.putIfNotExist(lockKey, token, maxLockLengthSeconds) ? LockState.Acquired : LockState.NotAcquired;
  }
}
