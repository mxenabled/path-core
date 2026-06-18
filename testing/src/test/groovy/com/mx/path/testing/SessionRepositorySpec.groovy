package com.mx.path.testing

import com.mx.path.core.context.Session
import com.mx.path.testing.SessionRepository

class SessionRepositorySpec extends SessionRepository {

  def "setupSessionRepository configures session suppliers"() {
    when:
    setupSessionRepository()

    then:
    Session.getRepositorySupplier() != null
    Session.getEncryptionServiceSupplier() != null

    cleanup:
    cleanupSessionRepository()
  }

  def "clearSessionRepository installs a fresh repository"() {
    given:
    setupSessionRepository()

    when:
    clearSessionRepository()

    then:
    Session.getRepositorySupplier() != null

    cleanup:
    cleanupSessionRepository()
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
