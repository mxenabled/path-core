package com.mx.testing

import static org.mockito.Mockito.validateMockitoUsage

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.mockito.MockitoAnnotations

trait WithMockery {
  @BeforeAll
  def initMockito() {
    MockitoAnnotations.initMocks(this)
  }

  @AfterAll
  def validateExpectations() {
    validateMockitoUsage()
  }
}
