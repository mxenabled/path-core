package com.mx.path.gateway.process

import static org.mockito.Mockito.*

import java.util.concurrent.Executors

import com.mx.common.security.EncryptionService
import com.mx.path.model.context.RequestContext
import com.mx.path.model.context.Session
import com.mx.path.model.context.store.SessionRepository
import com.mx.path.model.context.tracing.CustomTracer

import io.opentracing.Tracer

import spock.lang.Specification

class AsyncWithGatewayContextTest extends Specification {
  SessionRepository repository

  def setup() {
    repository = Mock()
    RequestContext.builder().clientId("Kasigi Yabu").build().register()
    Session.setRepositorySupplier({ -> repository })
    CustomTracer.setTracer(mock(Tracer))
  }

  def cleanup() {
    RequestContext.clear()
    Session.setRepositorySupplier(null)
    Session.setEncryptionServiceSupplier(null)
    Session.clearSession()
    CustomTracer.setTracer(null)
  }

  def "calls the supplied lambda"() {
    given:
    AsyncWithGatewayContext<String> subject = new AsyncWithGatewayContext<String>({ -> "Wakarimasu ka?" })

    when:
    def result = subject.execute()

    then:
    result == "Wakarimasu ka?"
  }

  def "returns a Future when submitted to an executor service"() {
    given:
    AsyncWithGatewayContext<String> subject = new AsyncWithGatewayContext<String>({ -> "Kinjiru!" })
    def executorService = Executors.newSingleThreadExecutor()

    when:
    def future = executorService.submit(subject)
    def result = future.get()

    then:
    result == "Kinjiru!"
  }
}
