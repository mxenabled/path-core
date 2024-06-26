package com.mx.path.gateway.context

import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.verify

import com.google.gson.Gson
import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.common.gateway.GatewayException
import com.mx.path.core.common.session.ServiceScope
import com.mx.path.core.context.Session
import com.mx.path.core.context.store.SessionRepository
import com.mx.path.gateway.Gateway
import com.mx.path.gateway.accessor.Accessor
import com.mx.path.gateway.accessor.AccessorConfiguration
import com.mx.testing.TestEncryptionService
import com.mx.testing.TestSessionRepository
import com.mx.testing.accessors.AccountBaseAccessor
import com.mx.testing.accessors.BaseAccessor
import com.mx.testing.accessors.proxy.BaseAccessorProxySingleton
import com.mx.testing.accessors.proxy.account.AccountBaseAccessorProxySingleton

import spock.lang.Specification

class ScopeTest extends Specification {
  Session subject
  Accessor accessor
  BaseAccessor baseAccessor
  Gateway gateway
  SessionRepository repository

  def setup() {
    accessor = new TestAccessor()
    baseAccessor = new TestBaseAccessor()
    gateway = mock(Gateway)
    repository = spy(new TestSessionRepository())
    def encryptionService = new TestEncryptionService(new ObjectMap())
    Session.setRepositorySupplier({ -> repository })
    Session.setEncryptionServiceSupplier({ -> encryptionService })
    doReturn(baseAccessor).when(gateway).getBaseAccessor()
    subject = new Session()
  }

  def cleanup() {
    GatewayRequestContext.clear()
    Session.setRepositorySupplier(null)
    Session.setEncryptionServiceSupplier(null)
    Session.clearSession()
  }

  def "Session"() {
    when:
    subject.put(Scope.Session, "key1", "value1")
    subject.sput(Scope.Session, "key2", "encrypted")
    subject.sputObj(Scope.Session, "key3", new TestObject("123"))

    then:
    subject.get(Scope.Session, "key1") == "value1"
    subject.get(Scope.Session, "key2") == "encrypted"
    subject.getObj(Scope.Session, "key3", TestObject.class).property == "123"
  }

  def "Service (BaseAccessor)"() {
    given:
    GatewayRequestContext.builder()
        .gateway(gateway)
        .currentAccessor(new TestAccessorNoServiceScope())
        .build()
        .register()

    when:
    subject.put(Scope.Service, "key1", "value1")
    subject.sput(Scope.Service, "key2", "encrypted")
    subject.sputObj(Scope.Service, "key3", new TestObject("123"))

    then:
    verify(repository).saveValue(subject, "BiggerBank.key1", "value1") || true
    verify(repository).saveValue(subject, "BiggerBank.key2", "encrypted") || true
    verify(repository).saveValue(subject, "BiggerBank.key3", new Gson().toJson(new TestObject("123"))) || true
    subject.get(Scope.Service, "key1") == "value1"
    subject.get(Scope.Service, "key2") == "encrypted"
    subject.getObj(Scope.Service, "key3", TestObject.class).property == "123"
  }

  def "Service (BaseAccessorProxy)"() {
    given:
    def baseAccessor = new BaseAccessorProxySingleton(AccessorConfiguration.builder().build(), TestBaseAccessor)
    GatewayRequestContext.builder()
        .gateway(gateway)
        .currentAccessor(baseAccessor)
        .build()
        .register()

    when:
    subject.put(Scope.Service, "key1", "value1")
    subject.sput(Scope.Service, "key2", "encrypted")
    subject.sputObj(Scope.Service, "key3", new TestObject("123"))

    then:
    verify(repository).saveValue(subject, "BiggerBank.key1", "value1") || true
    verify(repository).saveValue(subject, "BiggerBank.key2", "encrypted") || true
    verify(repository).saveValue(subject, "BiggerBank.key3", new Gson().toJson(new TestObject("123"))) || true
    subject.get(Scope.Service, "key1") == "value1"
    subject.get(Scope.Service, "key2") == "encrypted"
    subject.getObj(Scope.Service, "key3", TestObject.class).property == "123"
  }

  def "Service (Accessor)"() {
    given:
    GatewayRequestContext.builder()
        .gateway(gateway)
        .currentAccessor(accessor)
        .build()
        .register()

    when:
    subject.put(Scope.Service, "key1", "value1")
    subject.sput(Scope.Service, "key2", "encrypted")
    subject.sputObj(Scope.Service, "key3", new TestObject("123"))

    then:
    verify(repository).saveValue(subject, "BigBank.key1", "value1") || true
    verify(repository).saveValue(subject, "BigBank.key2", "encrypted") || true
    verify(repository).saveValue(subject, "BigBank.key3", new Gson().toJson(new TestObject("123"))) || true
    subject.get(Scope.Service, "key1") == "value1"
    subject.get(Scope.Service, "key2") == "encrypted"
    subject.getObj(Scope.Service, "key3", TestObject.class).property == "123"
  }

  def "Service (AccessorProxy)"() {
    given:
    GatewayRequestContext.builder()
        .gateway(gateway)
        .currentAccessor(new AccountBaseAccessorProxySingleton(AccessorConfiguration.builder().build(), TestAccessor))
        .build()
        .register()

    when:
    subject.put(Scope.Service, "key1", "value1")
    subject.sput(Scope.Service, "key2", "encrypted")
    subject.sputObj(Scope.Service, "key3", new TestObject("123"))

    then:
    verify(repository).saveValue(subject, "BigBank.key1", "value1") || true
    verify(repository).saveValue(subject, "BigBank.key2", "encrypted") || true
    verify(repository).saveValue(subject, "BigBank.key3", new Gson().toJson(new TestObject("123"))) || true
    subject.get(Scope.Service, "key1") == "value1"
    subject.get(Scope.Service, "key2") == "encrypted"
    subject.getObj(Scope.Service, "key3", TestObject.class).property == "123"
  }

  def "Service (Fallback)"() {
    given:
    GatewayRequestContext.builder().build().register()

    when:
    subject.put(Scope.Service, "key1", "value1")
    subject.sput(Scope.Service, "key2", "encrypted")
    subject.sputObj(Scope.Service, "key3", new TestObject("123"))

    then:
    verify(repository).saveValue(subject, "Session.key1", "value1") || true
    verify(repository).saveValue(subject, "Session.key2", "encrypted") || true
    verify(repository).saveValue(subject, "Session.key3", new Gson().toJson(new TestObject("123"))) || true
    subject.get(Scope.Service, "key1") == "value1"
    subject.get(Scope.Service, "key2") == "encrypted"
    subject.getObj(Scope.Service, "key3", TestObject.class).property == "123"
  }

  def "Service (throws exception when BaseAccessor doesn't have @ServiceScope)"() {
    given:
    GatewayRequestContext.builder()
        .gateway(gateway)
        .currentAccessor(new TestAccessorNoServiceScope())
        .build()
        .register()

    doReturn(new TestBaseAccessorNoServiceScope()).when(gateway).getBaseAccessor()

    when:
    subject.put(Scope.Service, "key1", "value1")

    then:
    thrown(GatewayException)
  }
}

class TestBaseAccessorNoServiceScope extends BaseAccessor {
  TestBaseAccessorNoServiceScope() {
  }
}

class TestAccessorNoServiceScope extends AccountBaseAccessor {
  TestAccessorNoServiceScope() {
  }
}

@ServiceScope("BigBank")
class TestAccessor extends AccountBaseAccessor {
  TestAccessor() {
  }
}

@ServiceScope("BiggerBank")
class TestBaseAccessor extends BaseAccessor {
  TestBaseAccessor() {
  }
}

class TestObject {
  def property

  TestObject(property) {
    this.property = property
  }
}
