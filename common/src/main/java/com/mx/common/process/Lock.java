package com.mx.common.process;

import java.io.Closeable;
import java.util.function.Supplier;

/**
 * Locking mechanism
 */
public abstract class Lock implements Closeable {

  public enum LockState {
    NotAcquired, Acquired, Timeout, ConditionMet
  }

  /**
   * Release the lock
   */
  @Override
  public abstract void close();

  /**
   * @return true, if lock is held by this process
   */
  public abstract boolean acquired();

  /**
   * Non-blocking request for lock
   * @return If lock acquired, then Acquired. Otherwise, NotAcquired
   */
  public abstract LockState request();

  /**
   * Blocks until lock acquired, or timeout
   * @return Acquired, if lock acquired. Timeout, if timeout exceeded.
   */
  public abstract LockState acquire();

  /**
   * Blocks until lock acquired, timeout, or condition is true.
   * @param waitUntil this condition is met
   * @return Acquired, if lock is acquired. Timeout, if timeout exceeded. ConditionMet, if condition is met.
   */
  public abstract LockState acquireOr(Supplier<Boolean> waitUntil);

}
