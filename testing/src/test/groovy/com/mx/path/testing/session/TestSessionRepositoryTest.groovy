package com.mx.path.testing.session

import com.mx.path.core.context.Session
import com.mx.path.testing.session.TestSessionRepository

import spock.lang.Specification

class TestSessionRepositoryTest extends Specification {
  TestSessionRepository subject
  Session session

  def setup() {
    subject = new TestSessionRepository()
    Session.setRepositorySupplier({ -> subject })
    session = new Session()
    session.setId("test-session-id")
    session.save()
  }

  def cleanup() {
    Session.clearSession()
    Session.setRepositorySupplier(null)
  }

  def "save and load round-trip"() {
    given:
    session.setUserId("user123")
    session.save()

    when:
    def loaded = subject.load("test-session-id")

    then:
    loaded != null
    loaded.getId() == "test-session-id"
  }

  def "load returns null for unknown session id"() {
    expect:
    subject.load("unknown-id") == null
  }

  def "saveValue and getValue"() {
    when:
    subject.saveValue(session, "myKey", "myValue")

    then:
    subject.getValue(session, "myKey") == "myValue"
  }

  def "getValue returns null for missing key"() {
    expect:
    subject.getValue(session, "nonexistent") == null
  }

  def "deleteValue removes a stored value"() {
    given:
    subject.saveValue(session, "key", "val")

    when:
    subject.deleteValue(session, "key")

    then:
    subject.getValue(session, "key") == null
  }

  def "delete removes the session"() {
    when:
    subject.delete(session)

    then:
    subject.load("test-session-id") == null
  }

  def "setIfNotExist stores value and returns true when key absent"() {
    when:
    def result = subject.setIfNotExist(session, "newKey", "val")

    then:
    result
    subject.getValue(session, "newKey") == "val"
  }

  def "setIfNotExist returns false and does not overwrite when key present"() {
    given:
    subject.saveValue(session, "existingKey", "original")

    when:
    def result = subject.setIfNotExist(session, "existingKey", "new")

    then:
    !result
    subject.getValue(session, "existingKey") == "original"
  }

  def "setIfNotExist with expiry stores value when key absent"() {
    when:
    def result = subject.setIfNotExist(session, "ttlKey", "val", 60_000L)

    then:
    result
    subject.getValue(session, "ttlKey") == "val"
  }

  def "setIfNotExist with expiry returns false when key present"() {
    given:
    subject.saveValue(session, "ttlKey", "original")

    when:
    def result = subject.setIfNotExist(session, "ttlKey", "new", 60_000L)

    then:
    !result
  }
}
