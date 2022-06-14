package com.mx.path.model.context;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

import com.mx.path.model.context.store.SessionRepository;

import org.apache.commons.lang.RandomStringUtils;

public class SessionRepositoryMutex extends SessionMutex {

  // Statics

  private static final int KEY_LENGTH = 30;
  private static final int POLL_MILLISECONDS = 50;

  // Fields

  private String key;
  private String lockValue;
  private SessionRepository repository;

  // Constructors

  public SessionRepositoryMutex(SessionRepository repository, String key) {
    this.key = key;
    this.lockValue = RandomStringUtils.randomAlphanumeric(KEY_LENGTH);
    this.repository = repository;
  }

  // Getter/setter

  public final String getKey() {
    return key;
  }

  public final String getLockValue() {
    return lockValue;
  }

  public final SessionRepository getRepository() {
    return repository;
  }

  // Public

  public final String currentLockValue() {
    return repository.getValue(Session.current(), key);
  }

  // SessionLock

  @Override
  public final LockState acquire(long milliseconds) {
    long start = System.currentTimeMillis();

    while (!acquired()) {
      if (milliseconds > 0 && (System.currentTimeMillis() - start) >= milliseconds) {
        return acquired() ? LockState.Acquired : LockState.Timeout;
      }

      request();

      try {
        Thread.sleep(POLL_MILLISECONDS);
      } catch (InterruptedException e) {
      }
    }

    return LockState.Acquired;
  }

  @Override
  public final boolean acquired() {
    return Objects.equals(lockValue, currentLockValue());
  }

  @Override
  public final LockState acquireOr(long milliseconds, Supplier<Boolean> waitUntil) {
    long start = System.currentTimeMillis();

    do {
      if (milliseconds > 0 && (System.currentTimeMillis() - start) >= milliseconds) {
        return acquired() ? LockState.Acquired : LockState.Timeout;
      }

      if (waitUntil.get()) {
        return LockState.ConditionMet;
      }

      request();

      if (acquired()) {
        return LockState.Acquired;
      }

      try {
        Thread.sleep(POLL_MILLISECONDS);
      } catch (InterruptedException e) {
      }
    } while (true);
  }

  @Override
  public final void close() throws IOException {
    if (acquired()) {
      repository.deleteValue(Session.current(), key);
    }
  }

  @Override
  public final LockState request() {
    return repository.setIfNotExist(Session.current(), key, lockValue) ? LockState.Acquired : LockState.NotAcquired;
  }
}
