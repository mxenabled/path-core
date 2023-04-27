package com.mx.path.testing

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.context.Session
import com.mx.path.core.context.store.SessionRepository
import com.mx.path.testing.session.TestEncryptionService
import com.mx.path.testing.session.TestSessionRepository

/**
 * Wired:
 *   before first: Sets fake session repository and encryption service
 *   after each: Sets clean session repository
 *
 * Usage:
 * <pre>
 *   class SessionieTest extends Specification implements WithSessionRepository {
 *   }
 * </pre>
 */
@SuppressFBWarnings("SE_NO_SERIALVERSIONID")
trait WithSessionRepository extends BaseTestingTrait {
  /**
   * Configures in-memory session repository and fake encryption service
   */
  def setupSessionRepository() {
    SessionRepository repository = new TestSessionRepository()
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
    SessionRepository repository = new TestSessionRepository()
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
