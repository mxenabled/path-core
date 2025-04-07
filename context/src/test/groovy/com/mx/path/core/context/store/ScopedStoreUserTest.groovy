package com.mx.path.core.context.store

import static org.mockito.Mockito.*

import com.mx.path.core.common.store.Store
import com.mx.path.core.context.Session
import com.mx.testing.WithMockery
import com.mx.testing.WithSessionRepository

import spock.lang.Specification

class ScopedStoreUserTest extends Specification  implements WithMockery, WithSessionRepository {

  Store store
  ScopedStoreUser subject

  def setup() {
    store = mock(Store)
    Session.createSession()
    Session session = Session.current()
    session.setUserId("user1")
    subject = new ScopedStoreUser(store, session)
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

  def "throwsExceptionOnNullSessionContext"() {
    when:
    subject = new ScopedStoreUser(store, null)
    subject.delete("key1")

    then:
    thrown(Exception)
  }
}
