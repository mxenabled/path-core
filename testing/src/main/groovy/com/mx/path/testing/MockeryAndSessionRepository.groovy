package com.mx.path.testing

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.context.Session
import com.mx.path.testing.session.TestEncryptionService
import com.mx.path.testing.session.TestSessionRepository

import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MockeryAndSessionRepository extends TestingBase {

  def setupMockery(){
    MockitoAnnotations.openMocks(this)
  }

  def cleanupMockery() {
    Mockito.validateMockitoUsage()
  }

  /**
   * Configures in-memory session repository and fake encryption service
   */
  def setupSessionRepository() {
    com.mx.path.core.context.store.SessionRepository repository = new TestSessionRepository()
    Session.setRepositorySupplier({ -> repository })
    TestEncryptionService testEncryptionService = new TestEncryptionService(new ObjectMap())
    Session.setEncryptionServiceSupplier({-> testEncryptionService})
  }

  /**
   * Sets clean session repository
   *
   * <p>Invoked after each test
   */
  def clearSessionRepository() {
    com.mx.path.core.context.store.SessionRepository repository = new TestSessionRepository()
    Session.setRepositorySupplier({ -> repository })
  }

  /**
   * Removes fake session repository and encryption service
   *
   * <p>Invoked once after class's last test
   */
  def cleanupSessionRepository() {
    Session.clearSession()
    Session.setRepositorySupplier({ -> null })
    Session.setEncryptionServiceSupplier({-> null})
  }
}
