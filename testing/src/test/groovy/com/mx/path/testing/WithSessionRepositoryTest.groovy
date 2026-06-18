package com.mx.path.testing

import com.mx.path.core.context.Session

import spock.lang.Specification

class WithSessionRepositoryTest extends Specification implements WithSessionRepository {

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

    cleanup:
    cleanupSessionRepository()
  }

  def "cleanupSessionRepository removes all suppliers"() {
    given:
    setupSessionRepository()

    when:
    cleanupSessionRepository()

    then:
    noExceptionThrown()
  }
}
