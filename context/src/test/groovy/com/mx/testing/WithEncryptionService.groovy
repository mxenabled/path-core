package com.mx.testing

import java.util.function.Supplier

import com.mx.path.core.common.security.EncryptionService
import com.mx.path.core.context.Session

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

trait WithEncryptionService {
  Supplier<EncryptionService> previousEncryptionServiceSupplier

  @BeforeAll
  def injectEncryptionService() {
    previousEncryptionServiceSupplier = Session.getEncryptionServiceSupplier()
    Session.setEncryptionServiceSupplier({ -> new EncryptionServiceImpl() })
  }

  @AfterAll
  def resetEncryptionService() {
    Session.setEncryptionServiceSupplier(previousEncryptionServiceSupplier)
  }
}