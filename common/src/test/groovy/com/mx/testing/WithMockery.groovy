package com.mx.testing

import static org.mockito.Mockito.validateMockitoUsage

import org.mockito.MockitoAnnotations

trait WithMockery {
  def setupSpec(){
    MockitoAnnotations.initMocks(this)
  }

  def cleanupSpec() {
    validateMockitoUsage()
  }
}
