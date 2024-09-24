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

  /**
   * -- GETTER --
   * Return lock key.
   *
   * @return lock key
   */
  @Getter
  private final String lockKey;

  /**
   * -- GETTER --
   * Return acquire timeout duration.
   *
   * @return acquire timeout duration
   * -- SETTER --
   * Set acquire timeout duration.
   *
   * @param acquireTimeoutMilliseconds acquire timetout duration to set
   */
  @Getter
  @Setter
  private long acquireTimeoutMilliseconds;

  /**
   * -- GETTER --
   * Return poll time.
   *
   * @return poll time
   * -- SETTER --
   * Set poll time.
   *
   * @param pollMilliseconds poll time to set
   */
  @Getter
  @Setter
  private long pollMilliseconds;

  /**
   * -- GETTER --
   * Return max lock length in seconds.
   *
   * @return max lock length in seconds
   * -- SETTER --
   * Set max lock length.
   *
   * @param maxLockLengthSeconds max lock length to set
   */
  @Getter
  @Setter
  private int maxLockLengthSeconds;
  private final Store store;

  /**
   * -- GETTER --
   * Return token.
   *
   * @return token
   */
  @Getter
  private final String token;

  // Constructors

  /**
   * Build new instance for {@link StoreLock}.
   *
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

  /**
   * Value associated with provided key.
   *
   * @return value
   */
  public final String currentLockValue() {
    return store.get(lockKey);
  }

  // SessionLock

  /**
   * Execute operation.
   *
   * @return lock state
   */
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

  /**
   * Checks if operation was acquired.
   *
   * @return true if acquired
   */
  @Override
  public final boolean acquired() {
    return Objects.equals(token, currentLockValue());
  }

  /**
   * Execute operation with condition.
   *
   * @param waitUntil this condition is met
   * @return current lock state
   */
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

  /**
   * Delete lock.
   */
  @Override
  public final void close() {
    if (acquired()) {
      store.delete(lockKey);
    }
  }

  /**
   * Execute.
   *
   * @return current lock state.
   */
  @Override
  public final LockState request() {
    return store.putIfNotExist(lockKey, token, maxLockLengthSeconds) ? LockState.Acquired : LockState.NotAcquired;
  }
}
