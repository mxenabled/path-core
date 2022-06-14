package com.mx.testing

import java.util.function.Supplier

import com.mx.common.security.EncryptionService
import com.mx.path.model.context.Session

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach


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