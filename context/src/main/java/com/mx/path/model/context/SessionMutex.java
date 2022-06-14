package com.mx.path.model.context;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Supplier;

@Deprecated
public abstract class SessionMutex implements Closeable {
  public enum LockState {
    NotAcquired, Acquired, Timeout, ConditionMet
  }

  /**
   * Release the mutex
   */
  @Override
  public abstract void close() throws IOException;

  /**
   * @return true, if mutex lock is held by this process
   */
  public abstract boolean acquired();

  /**
   * Non-blocking request for mutex
   * @return If lock acquired, then Acquired. Otherwise, NotAcquired
   */
  public abstract LockState request();

  /**
   * Blocks until mutex lock acquired, or timeout
   * @param milliseconds to wait for acquire
   * @return Acquired, if mutex acquired. Timeout, if timed out.
   */
  public abstract LockState acquire(long milliseconds);

  /**
   * Blocks until mutex lock acquired, timeout, or condition is true.
   * @param milliseconds to wait
   * @param waitUntil this condition is met
   * @return Acquired, if mutex lock is acquired. Timeout, if timeout. ConditionMet, if condition is met.
   */
  public abstract LockState acquireOr(long milliseconds, Supplier<Boolean> waitUntil);
}
