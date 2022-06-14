package com.mx.path.model.context

import com.mx.path.model.context.SessionMutex.LockState
import com.mx.testing.HashSessionRepository

import spock.lang.Specification

class SessionRepositoryMutexTest extends Specification {
  LockState lockState
  HashSessionRepository repository
  Session session


  def setup() {
    repository = new HashSessionRepository()
    Session.setRepositorySupplier({ -> repository })
    Session.createSession()
    session = Session.current()
    session.save()
  }

  def cleanup() {
    Session.clearSession()
    Session.setRepositorySupplier(null)
  }

  def "lockValueIsUnique"() {
    given:
    def mutex1 = new SessionRepositoryMutex(repository, "key1")
    def mutex2 = new SessionRepositoryMutex(repository, "key1")

    expect:
    mutex1.getLockValue() != mutex2.getLockValue()
  }

  def "keyIsSet"() {
    given:
    def mutex = new SessionRepositoryMutex(repository, "key1")

    expect:
    "key1" == mutex.getKey()
  }

  def "currentLockValueGetsFromRepository"() {
    given:
    repository.saveValue(session, "key1", "123456")
    def mutex = new SessionRepositoryMutex(repository, "key1")

    expect:
    "123456" == mutex.currentLockValue()
    "123456" != mutex.getLockValue()
  }

  def "acquireFirst"() {
    given:
    def mutex = new SessionRepositoryMutex(repository, "key1")
    mutex.acquire(100)

    expect: "This lock value should be set in repository"
    mutex.acquired()
    repository.getValue(session, "key1") == mutex.getLockValue()
  }

  def "acquireWithNoTimeout"() {
    given:
    def mutex = new SessionRepositoryMutex(repository, "key1")
    mutex.acquire(0)

    expect: "This lock value should be set in repository"
    mutex.acquired()
    repository.getValue(session, "key1") == mutex.getLockValue()
  }

  def "acquireWithTimeoutElapse"() {
    given:
    repository.saveValue(session, "key1", "123456")
    def mutex = new SessionRepositoryMutex(repository, "key1")
    def acquiringThread = new Thread({
      ->
      Session.loadSession(session.getId())
      lockState = mutex.acquire(100)
    })

    when:
    acquiringThread.start()
    Thread.sleep(150)
    acquiringThread.join()

    then:
    SessionMutex.LockState.Timeout == lockState
  }

  def "acquireWithTimeout"() {
    given: "Another process acquires lock"
    repository.saveValue(session, "key1", "123456")
    def mutex = new SessionRepositoryMutex(repository, "key1")
    Thread acquiringThread = new Thread({
      ->
      Session.loadSession(session.getId())
      lockState = mutex.acquire(1000)
    })
    acquiringThread.start()
    Thread.sleep(50)

    when: "Release the lock"
    repository.deleteValue(session, "key1")
    acquiringThread.join()

    then:
    LockState.Acquired == lockState
  }

  def "acquireOrFirst"() {
    given:
    def mutex = new SessionRepositoryMutex(repository, "key1")
    mutex.acquireOr(100, { -> false })

    expect: "This lock value should be set in repository"
    mutex.acquired()
    repository.getValue(session, "key1") == mutex.getLockValue()
  }

  def "acquireOrWithNoTimeout"() {
    given:
    def mutex = new SessionRepositoryMutex(repository, "key1")
    mutex.acquireOr(0, { -> false })

    expect: "This lock value should be set in repository"
    mutex.acquired()
    repository.getValue(session, "key1") == mutex.getLockValue()
  }

  def "acquireOrWithTimeoutElapse"() {
    given: "Another process acquires lock"
    repository.saveValue(session, "key1", "123456")
    def mutex = new SessionRepositoryMutex(repository, "key1")
    def acquiringThread = new Thread({
      ->
      Session.loadSession(session.getId())
      lockState = mutex.acquireOr(100, { -> false })
    })
    acquiringThread.start()
    Thread.sleep(150)
    acquiringThread.join()

    expect:
    LockState.Timeout == lockState
  }

  def "acquireOrWithTimeout"() {
    given: "Another process acquires lock"
    repository.saveValue(session, "key1", "123456")
    def mutex = new SessionRepositoryMutex(repository, "key1")
    def acquiringThread = new Thread({
      ->
      Session.loadSession(session.getId())
      lockState = mutex.acquireOr(1000, { -> false })
    })
    acquiringThread.start()
    Thread.sleep(50)

    when: "Release the lock"
    repository.deleteValue(session, "key1")
    acquiringThread.join()

    then:
    LockState.Acquired == lockState
  }

  def "acquireOrWithConditionMet"() {
    given: "Another process acquires lock"
    repository.saveValue(session, "key1", "123456")
    def mutex = new SessionRepositoryMutex(repository, "key1")
    def acquiringThread = new Thread({
      ->
      Session.loadSession(session.getId())
      lockState = mutex.acquireOr(1000, { -> Objects.equals(repository.getValue(session, "waitForIt"), "done") })
    })
    acquiringThread.start()
    Thread.sleep(50)

    when: "Release the lock"
    repository.saveValue(session, "waitForIt", "done")
    acquiringThread.join()

    then:
    LockState.ConditionMet == lockState
  }

  def "requestFirst"() {
    given:
    def mutex = new SessionRepositoryMutex(repository, "key1")
    lockState = mutex.request()

    expect: "This lock value should be set in repository"
    LockState.Acquired == lockState
    mutex.acquired()
    repository.getValue(session, "key1") == mutex.getLockValue()
  }

  def "requestSecond"() {
    given: "Another process acquires the lock"
    repository.saveValue(session, "key1", "123456")
    def mutex = new SessionRepositoryMutex(repository, "key1")
    lockState = mutex.request()
    assert LockState.NotAcquired == lockState
    !mutex.acquired()

    when: "First frees the lock"
    repository.deleteValue(session, "key1")
    lockState = mutex.request()

    then:
    LockState.Acquired == lockState
    mutex.acquired()
  }

  def "closeReleasesThisLock"() {
    given:
    def mutex = new SessionRepositoryMutex(repository, "key1")
    mutex.acquire(100)
    assert repository.getValue(session, "key1") != null

    when:
    mutex.close()

    then: "Should remove key from repository"
    repository.getValue(session, "key1") == null
  }

  def "closeDoesNotReleaseOtherLock"() {
    given: "Another process acquires the lock"
    repository.saveValue(session, "key1", "123456")
    def mutex = new SessionRepositoryMutex(repository, "key1")
    mutex.acquire(100)
    assert "123456" == repository.getValue(session, "key1")

    expect: "Should leave the first guy's key"
    mutex.close()
    "123456" == repository.getValue(session, "key1")
  }
}