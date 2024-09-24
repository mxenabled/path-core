package com.mx.path.core.common.process;

import java.io.Closeable;
import java.util.function.Supplier;

/**
 * Locking mechanism.
 */
public abstract class Lock implements Closeable {

  /**
   * Enum for lock states.
   */
  public enum LockState {
    /**
     * Lock has not been acquired.
     */
    NotAcquired,
    /**
     * Lock has been successfully acquired.
     */
    Acquired,
    /**
     * Lock acquisition attempt timed out.
     */
    Timeout,
    /**
     * Condition passed into the {@link #acquireOr(Supplier)} method has been met.
     */
    ConditionMet
  }

  /**
   * Release the lock.
   */
  @Override
  public abstract void close();

  /**
   * Check if lock is currently held by the calling process.
   *
   * @return true if lock is held by this process
   */
  public abstract boolean acquired();

  /**
   * Non-blocking request for lock.
   *
   * @return if lock acquired, then Acquired. Otherwise, NotAcquired
   */
  public abstract LockState request();

  /**
   * Blocks until lock acquired, or timeout.
   *
   * @return Acquired, if lock acquired. Timeout, if timeout exceeded.
   */
  public abstract LockState acquire();

  /**
   * Blocks until lock acquired, timeout, or condition is true.
   *
   * @param waitUntil this condition is met
   * @return Acquired, if lock is acquired. Timeout, if timeout exceeded. ConditionMet, if condition is met.
   */
  public abstract LockState acquireOr(Supplier<Boolean> waitUntil);

}
