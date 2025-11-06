package com.mx.path.testing

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * Base Trait for Path testing traits. Need to register setup and teardown methods here.
 * This needs to be done because Spock will not execute all included setupSpec and cleanupSpec
 * methods. Only the last one in. :(
 *
 * @deprecated Use {@link com.mx.path.testing.TestingBase}
 */
@Deprecated
trait BaseTestingTrait {

  /**
   * Executes once before the class's test is run (NOT before each test)
   *
   * @deprecated Use {@link com.mx.path.testing.TestingBase#setupSpec()}
   */
  @Deprecated
  def setupSpec() {
    safeInvoke("setupMockery")
    safeInvoke("setupSessionRepository")
  }

  /**
   * Runs once after the class's last test is complete
   */
  def cleanupSpec() {
  }

  /**
   * Runs before each test
   *
   * Caution: This will execute before test but AFTER the class's setup method.
   *          If the trait needs to be setup before that, then it needs to be invoked in the setup method
   *          by the spec writer. Document this in trait's javadocs
   */
  @BeforeEach()
  def testSetup() {
  }

  /**
   * Runs after each test
   *
   * @deprecated Use {@link com.mx.path.testing.TestingBase#cleanup()}
   */
  @AfterEach()
  @Deprecated
  def testCleanup() {
    safeInvoke("cleanupRequestExpectations")
    safeInvoke("cleanupMockery")
    safeInvoke("clearSessionRepository")
  }

  private def safeInvoke(String methodName) {
    if (respondsTo(methodName)) {
      println("Invoking ${methodName}")
      invokeMethod(methodName, [])
    }
  }
}
