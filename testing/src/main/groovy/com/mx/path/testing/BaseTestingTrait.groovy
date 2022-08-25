package com.mx.path.testing

import java.util.function.BooleanSupplier
import java.util.function.Consumer

import groovy.transform.PackageScope

import com.mx.common.collections.ObjectMap
import com.mx.path.model.context.Session
import com.mx.path.model.context.store.SessionRepository

import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.MockitoAnnotations

import spock.lang.Shared

/**
 * Base Trait for Path testing traits. Need to register setup and teardown methods here.
 * This needs to be done because Spock will not execute all included setupSpec and cleanupSpec
 * methods. Only the last one in. :(
 */
trait BaseTestingTrait {

  /**
   * Executes once before the class's test is run (NOT before each test)
   */
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
  @Before()
  def testSetup() {
  }

  /**
   * Runs after each test
   */
  @After()
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
