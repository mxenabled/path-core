package com.mx.path.core.context.store


import static org.mockito.Mockito.*

import com.mx.path.core.common.store.Store
import com.mx.testing.WithMockery
import com.mx.testing.WithSessionRepository

import spock.lang.Specification

class ScopedStoreCurrentUserTest extends Specification  implements WithMockery, WithSessionRepository {

  Store store
  ScopedStoreCurrentUser subject

  def setup() {
    store = mock(Store)
    subject = new ScopedStoreCurrentUser(store)
    com.mx.path.core.context.Session.createSession()
    com.mx.path.core.context.Session.current().setUserId("user1")
  }

  def "delete"() {
    when:
    subject.delete("key1")

    then:
    verify(store).delete("user1:key1") || true
  }

  def "get"() {
    when:
    subject.get("key1")

    then:
    verify(store).get("user1:key1") || true
  }

  def "put"() {
    when:
    subject.put("key1", "value1", 10)

    then:
    verify(store).put("user1:key1", "value1", 10) || true
  }

  def "put without TTL"() {
    when:
    subject.put("key1", "value1")

    then:
    verify(store).put("user1:key1", "value1") || true
  }

  def "putIfNotExist"() {
    when:
    when(store.putIfNotExist("user1:key1", "value1", 10)).thenReturn(true)
    def result = subject.putIfNotExist("key1", "value1", 10)

    then:
    verify(store).putIfNotExist("user1:key1", "value1", 10) || true
    result
  }

  def "putIfNotExist without TTL"() {
    when:
    when(store.putIfNotExist("user1:key1", "value1")).thenReturn(true)
    def result = subject.putIfNotExist("key1", "value1")

    then:
    verify(store).putIfNotExist("user1:key1", "value1") || true
    result
  }

  def "throws exception on null Session"() {
    when:
    com.mx.path.core.context.Session.clearSession()
    subject.delete("key1")

    then:
    def e = thrown(com.mx.path.core.context.GatewayContextException)
    e.message == "Attempting to read userId from null Session"
  }

  def "throws exception on null Session#userId"() {
    when:
    com.mx.path.core.context.Session.current().userId = null
    subject.delete("key1")

    then:
    def e = thrown(com.mx.path.core.context.GatewayContextException)
    e.message == "Session#userId must be non-null to use ScopedStoreCurrentUser"
  }
}
