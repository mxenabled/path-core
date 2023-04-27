package com.mx.path.gateway.process


import static org.mockito.Mockito.mock
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.verify

import java.util.concurrent.Executors

import com.mx.path.core.common.gateway.GatewayException
import com.mx.path.core.context.RequestContext
import com.mx.path.core.context.Session
import com.mx.path.core.context.store.SessionRepository
import com.mx.path.core.context.tracing.CustomTracer

import io.opentracing.Tracer

import spock.lang.Specification

class FutureWithGatewayContextTest extends Specification {
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
    def subject = new FutureWithGatewayContext<String>({ -> "Minha Nossa!" })

    when:
    def result = subject.get()

    then:
    result == "Minha Nossa!"
  }

  def "can pass in a custom executor"() {
    given:
    def executorService = spy(Executors.newSingleThreadExecutor())
    def lambda = { -> "Caramba!"}
    def subject = new FutureWithGatewayContext<String>(lambda, executorService)

    when:
    def result = subject.get()

    then:
    result == "Caramba!"
    verify(executorService).submit(new AsyncWithGatewayContext<String>(lambda)) || true
  }

  def "can override the timeout"() {
    given:
    def subject = new FutureWithGatewayContext<String>({
      ->
      Thread.sleep(500)
      return "Churrasco!"
    }, 1)

    when:
    def result = subject.get()

    then:
    def e = thrown(GatewayException)
    e.message == "FutureWithGatewayContext timeout out"
  }
}
