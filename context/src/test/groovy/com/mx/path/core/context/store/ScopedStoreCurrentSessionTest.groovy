package com.mx.path.core.context.store


import static org.mockito.Mockito.*

import com.mx.path.core.common.store.Store
import com.mx.testing.WithMockery
import com.mx.testing.WithSessionRepository

import spock.lang.Specification

class ScopedStoreCurrentSessionTest extends Specification implements WithMockery, WithSessionRepository {

  Store store
  ScopedStoreCurrentSession subject
  String sessionId

  def setup() {
    store = mock(Store)
    subject = new ScopedStoreCurrentSession(store)
    com.mx.path.core.context.Session.createSession()
    sessionId = com.mx.path.core.context.Session.current().getId()
  }

  def "delete"() {
    when:
    subject.delete("key1")

    then:
    verify(store).delete("${sessionId}:key1") || true
  }

  def "get"() {
    when:
    subject.get("key1")

    then:
    verify(store).get("${sessionId}:key1") || true
  }

  def "put"() {
    when:
    subject.put("key1", "value1", 10)

    then:
    verify(store).put("${sessionId}:key1", "value1", 10) || true
  }

  def "put without TTL"() {
    when:
    subject.put("key1", "value1")

    then:
    verify(store).put("${sessionId}:key1", "value1") || true
  }

  def "putIfNotExist"() {
    when:
    when(store.putIfNotExist("${sessionId}:key1", "value1", 10)).thenReturn(true)
    def result = subject.putIfNotExist("key1", "value1", 10)

    then:
    verify(store).putIfNotExist("${sessionId}:key1", "value1", 10) || true
    result
  }

  def "putIfNotExist without TTL"() {
    when:
    when(store.putIfNotExist("${sessionId}:key1", "value1")).thenReturn(true)
    def result = subject.putIfNotExist("key1", "value1")

    then:
    verify(store).putIfNotExist("${sessionId}:key1", "value1") || true
    result
  }

  def "getSet"() {
    when:
    subject.getSet("key1")

    then:
    verify(store).getSet("${sessionId}:key1") || true
  }

  def "putSet"() {
    when:
    subject.putSet("key1", "value1", 10)

    then:
    verify(store).putSet("${sessionId}:key1", "value1", 10) || true
  }

  def "putSet without TTL"() {
    when:
    subject.putSet("key1", "value1")

    then:
    verify(store).putSet("${sessionId}:key1", "value1") || true
  }

  def "deleteSet"() {
    when:
    subject.deleteSet("key1", "value1")

    then:
    verify(store).deleteSet("${sessionId}:key1", "value1") || true
  }

  def "inSet"() {
    when:
    subject.inSet("key1", "value1")

    then:
    verify(store).inSet("${sessionId}:key1", "value1") || true
  }

  def "throws exception on null Session"() {
    when:
    com.mx.path.core.context.Session.clearSession()
    subject.delete("key1")

    then:
    def e = thrown(com.mx.path.core.context.GatewayContextException)
    e.message == "Attempting to read id from null Session"
  }

  def "throws exception on null Session#id"() {
    when:
    com.mx.path.core.context.Session.current().id = null
    subject.delete("key1")

    then:
    def e = thrown(com.mx.path.core.context.GatewayContextException)
    e.message == "Session#id must be non-null to use ScopedStoreCurrentSession"
  }
}