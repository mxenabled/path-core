package com.mx.path.core.context.store

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.common.process.Lock
import com.mx.path.core.common.store.Store
import com.mx.testing.StoreImpl
import com.mx.testing.WithMockery

import spock.lang.Specification

class StoreLockTest extends Specification implements WithMockery {

  Store store
  StoreLock subject
  ObjectMap configurations

  def setup() {
    store = new StoreImpl();
    configurations = new ObjectMap()
    subject = new StoreLock(store, "key1", configurations)
  }

  def "lockValueIsUnique"() {
    given:
    def mutex1 = new StoreLock(store, "key1", configurations)
    def mutex2 = new StoreLock(store, "key1", configurations)

    expect:
    mutex1.getToken() != mutex2.getToken()
  }

  def "keyIsSet"() {
    given:
    def mutex = new StoreLock(store, "key1", configurations)

    expect:
    "lock_token:key1" == mutex.getLockKey()
  }

  def "acquireFirst"() {
    given:
    def mutex = new StoreLock(store, "key1", configurations)
    mutex.acquire()

    expect: "This lock value should be set in store"
    mutex.acquired()
    store.get("lock_token:key1") == mutex.getToken()
  }

  def "acquireWithTimeoutElapse"() {
    given:
    def lockState = Lock.LockState.NotAcquired
    store.put("lock_token:key1", "123456", 0)
    def mutex = new StoreLock(store, "key1", configurations)
    mutex.setAcquireTimeoutMilliseconds(10)
    def acquiringThread = new Thread({
      ->
      lockState = mutex.acquire()
    })

    when:
    acquiringThread.start()
    Thread.sleep(150)
    acquiringThread.join()

    then:
    Lock.LockState.Timeout == lockState
  }

  def "acquireWithTimeout"() {
    given: "Another process acquires lock"
    store.put("lock_token:key1", "123456", 0)
    def mutex = new StoreLock(store, "key1", configurations)
    mutex.setAcquireTimeoutMilliseconds(1000)
    def lockState = Lock.LockState.NotAcquired
    Thread acquiringThread = new Thread({
      ->
      lockState = mutex.acquire()
    })
    acquiringThread.start()
    Thread.sleep(50)

    when: "Release the lock"
    store.delete("lock_token:key1")
    acquiringThread.join()

    then:
    Lock.LockState.Acquired == lockState
  }

  def "acquireOrFirst"() {
    given:
    def mutex = new StoreLock(store, "key1", configurations)
    mutex.setAcquireTimeoutMilliseconds(100)
    mutex.acquireOr({ -> false })

    expect: "This lock value should be set in store"
    mutex.acquired()
    store.get("lock_token:key1") == mutex.getToken()
  }

  def "acquireOrWithNoTimeout"() {
    given:
    def mutex = new StoreLock(store, "key1", configurations)
    mutex.setAcquireTimeoutMilliseconds(0)
    mutex.acquireOr({ -> false })

    expect: "This lock value should be set in store"
    mutex.acquired()
    store.get("lock_token:key1") == mutex.getToken()
  }

  def "acquireOrWithTimeoutElapse"() {
    given: "Another process acquires lock"
    store.put("lock_token:key1", "123456", 0)
    def lockState = Lock.LockState.NotAcquired
    def mutex = new StoreLock(store, "key1", configurations)
    mutex.setAcquireTimeoutMilliseconds(100)
    def acquiringThread = new Thread({
      ->
      lockState = mutex.acquireOr({ -> false })
    })
    acquiringThread.start()
    Thread.sleep(150)
    acquiringThread.join()

    expect:
    Lock.LockState.Timeout == lockState
  }

  def "acquireOrWithTimeout"() {
    given: "Another process acquires lock"
    store.put("lock_token:key1", "123456", 0)
    def lockState = Lock.LockState.NotAcquired
    def mutex = new StoreLock(store, "key1", configurations)
    mutex.setAcquireTimeoutMilliseconds(1000)
    def acquiringThread = new Thread({
      ->
      lockState = mutex.acquireOr({ -> false })
    })
    acquiringThread.start()
    Thread.sleep(50)

    when: "Release the lock"
    store.delete("lock_token:key1")
    acquiringThread.join()

    then:
    Lock.LockState.Acquired == lockState
  }

  def "acquireOrWithConditionMet"() {
    given: "Another process acquires lock"
    store.put("lock_token:key1", "123456", 0)
    def lockState = Lock.LockState.NotAcquired
    def mutex = new StoreLock(store, "key1", configurations)
    mutex.setAcquireTimeoutMilliseconds(1000)
    def acquiringThread = new Thread({
      ->
      lockState = mutex.acquireOr({ -> Objects.equals(store.get("waitForIt"), "done") })
    })
    acquiringThread.start()
    Thread.sleep(50)

    when: "Release the lock"
    store.put("waitForIt", "done", 0)
    acquiringThread.join()

    then:
    Lock.LockState.ConditionMet == lockState
  }

  def "requestFirst"() {
    given:
    def mutex = new StoreLock(store, "key1", configurations)
    def lockState = mutex.request()

    expect: "This lock value should be set in store"
    Lock.LockState.Acquired == lockState
    mutex.acquired()
    store.get("lock_token:key1") == mutex.getToken()
  }

  def "requestSecond"() {
    given: "Another process acquires the lock"
    store.put("lock_token:key1", "123456", 0)
    def mutex = new StoreLock(store, "key1", configurations)
    def lockState = mutex.request()
    assert Lock.LockState.NotAcquired == lockState
    !mutex.acquired()

    when: "First frees the lock"
    store.delete("lock_token:key1")
    lockState = mutex.request()

    then:
    Lock.LockState.Acquired == lockState
    mutex.acquired()
  }

  def "closeReleasesThisLock"() {
    given:
    def mutex = new StoreLock(store, "key1", configurations)
    mutex.setAcquireTimeoutMilliseconds(100)
    mutex.acquire()
    assert store.get("lock_token:key1") != null

    when:
    mutex.close()

    then: "Should remove key from store"
    store.get("lock_token:key1") == null
  }

  def "closeDoesNotReleaseOtherLock"() {
    given: "Another process acquires the lock"
    store.put("lock_token:key1", "123456", 0)
    def mutex = new StoreLock(store, "key1", configurations)
    mutex.setAcquireTimeoutMilliseconds(100)
    mutex.acquire()
    assert "123456" == store.get("lock_token:key1")

    expect: "Should leave the first guy's key"
    mutex.close()
    "123456" == store.get("lock_token:key1")
  }
}