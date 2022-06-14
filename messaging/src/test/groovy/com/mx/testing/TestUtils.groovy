package com.mx.testing

import static org.mockito.Mockito.mock

import com.mx.path.model.context.RequestContext
import com.mx.path.model.context.Session
import com.mx.path.model.context.tracing.CustomTracer
import com.mx.testing.fakes.FakeSessionRepository

import io.opentracing.Tracer

class TestUtils {
  static startFakedSession() {
    def sessionRepository = new FakeSessionRepository()
    Session.setRepositorySupplier({ -> sessionRepository })
    CustomTracer.setTracer(mock(Tracer))

    Session.createSession()
    RequestContext.builder().clientId("clientId").build().register()
  }

  static endFakedSession() {
    Session.setRepositorySupplier({ -> null })
    Session.setEncryptionServiceSupplier({ -> null })
    CustomTracer.setTracer(null)
    Session.clearSession()
    RequestContext.clear()
  }
}
