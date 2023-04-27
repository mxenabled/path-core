package com.mx.testing

import com.mx.path.core.context.Session
import com.mx.path.core.context.store.SessionRepository

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

trait WithSessionRepository {
  SessionRepository sessionRepository = new TestSessionRepository()

  @BeforeAll
  def injectSessionRepository() {
    Session.setRepositorySupplier({ -> sessionRepository })
  }

  @AfterAll
  def resetSession() {
    Session.clearSession()
    Session.setRepositorySupplier(null)
  }
}
