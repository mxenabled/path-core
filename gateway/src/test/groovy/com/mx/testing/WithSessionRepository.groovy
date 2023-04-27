package com.mx.testing

import com.mx.path.core.context.Session
import com.mx.path.core.context.store.SessionRepository

trait WithSessionRepository {
  SessionRepository sessionRepository

  def setupSpec() {
    sessionRepository = new TestSessionRepository()
    Session.setRepositorySupplier({ -> sessionRepository })
  }

  def cleanupSpec() {
    Session.clearSession()
    Session.setRepositorySupplier(null)
  }
}