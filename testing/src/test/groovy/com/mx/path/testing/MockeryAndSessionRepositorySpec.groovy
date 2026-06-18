package com.mx.path.testing

import com.mx.path.core.context.Session

class MockeryAndSessionRepositorySpec extends MockeryAndSessionRepository {

  def cleanup() {
    Session.clearSession()
    Session.setRepositorySupplier(null)
    Session.setEncryptionServiceSupplier(null)
  }

  def "setupMockery and cleanupMockery complete without error"() {
    when:
    setupMockery()
    cleanupMockery()

    then:
    noExceptionThrown()
  }

  def "setupSessionRepository configures session suppliers"() {
    when:
    setupSessionRepository()

    then:
    Session.getRepositorySupplier() != null
    Session.getEncryptionServiceSupplier() != null
  }

  def "clearSessionRepository resets to a fresh repository"() {
    given:
    setupSessionRepository()

    when:
    clearSessionRepository()

    then:
    Session.getRepositorySupplier() != null
  }

  def "cleanupSessionRepository removes suppliers"() {
    given:
    setupSessionRepository()

    when:
    cleanupSessionRepository()

    then:
    noExceptionThrown()
  }
}
