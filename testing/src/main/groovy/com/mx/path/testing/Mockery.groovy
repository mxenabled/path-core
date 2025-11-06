package com.mx.path.testing

import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class Mockery extends TestingBase {
  def setupMockery(){
    MockitoAnnotations.openMocks(this)
  }

  def cleanupMockery() {
    Mockito.validateMockitoUsage()
  }
}
