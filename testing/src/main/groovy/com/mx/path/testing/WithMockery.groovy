package com.mx.path.testing


import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Wired:
 *   after each: validates expectations
 * Available:
 *   setupMockery: usually not needed, can be invoked in setup() to configure some mocking features.
 *
 * Usage:
 * <pre>
 *   class MockieTest extends Specification implements WithMockery {
 *     def setup() {
 *       // optional
 *       setupMockery()
 *     }
 *   }
 * </pre>
 *
 * @deprecated Use {@link com.mx.path.testing.Mockery}
 */
@Deprecated
trait WithMockery extends BaseTestingTrait {
  def setupMockery(){
    MockitoAnnotations.openMocks(this)
  }

  def cleanupMockery() {
    Mockito.validateMockitoUsage()
  }
}
