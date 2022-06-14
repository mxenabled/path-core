package com.mx.testing

import static org.mockito.Mockito.validateMockitoUsage

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.MockitoAnnotations

trait WithMockery {
  @BeforeEach
  def initMockito() {
    MockitoAnnotations.openMocks(this)
  }

  @AfterEach
  def validateExpectations() {
    validateMockitoUsage()
  }
}
