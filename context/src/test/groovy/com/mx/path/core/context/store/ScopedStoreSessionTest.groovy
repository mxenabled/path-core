package com.mx.path.core.context.store


import static org.mockito.Mockito.*

import com.mx.path.core.common.store.Store

import spock.lang.Specification

class ScopedStoreSessionTest extends Specification {
  Store store
  ScopedStoreSession subject

  def setup() {
    store = mock(Store)
    com.mx.path.core.context.Session.createSession()
    com.mx.path.core.context.Session.current().setId("sessionId")
    subject = new ScopedStoreSession(store, com.mx.path.core.context.Session.current())
  }

  def "delete"() {
    when:
    subject.delete("key1")

    then:
    verify(store).delete("sessionId:key1") || true
  }

  def "get"() {
    when:
    subject.get("key1")

    then:
    verify(store).get("sessionId:key1") || true
  }

  def "put"() {
    when:
    subject.put("key1", "value1", 10)

    then:
    verify(store).put("sessionId:key1", "value1", 10) || true
  }

  def "put without TTL"() {
    when:
    subject.put("key1", "value1")

    then:
    verify(store).put("sessionId:key1", "value1") || true
  }

  def "putIfNotExist"() {
    when:
    when(store.putIfNotExist("sessionId:key1", "value1", 10)).thenReturn(true)
    def result = subject.putIfNotExist("key1", "value1", 10)

    then:
    verify(store).putIfNotExist("sessionId:key1", "value1", 10) || true
    result
  }

  def "putIfNotExist without TTL"() {
    when:
    when(store.putIfNotExist("sessionId:key1", "value1")).thenReturn(true)
    def result = subject.putIfNotExist("key1", "value1")

    then:
    verify(store).putIfNotExist("sessionId:key1", "value1") || true
    result
  }

  def "throwsExceptionOnNullSessionContext"() {
    when:
    RequestContext.builder().clientId("clientId").build().register()
    subject = new ScopedStoreSession(store, null)
    subject.delete("key1")

    then:
    thrown(RuntimeException)
  }
}
