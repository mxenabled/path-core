package com.mx.path.model.context.store

import static org.mockito.Mockito.*

import com.mx.common.store.Store
import com.mx.testing.WithMockery

import spock.lang.Specification

class ScopedStoreGlobalTest extends Specification implements WithMockery {
  Store store
  ScopedStoreGlobal subject

  def setup() {
    store = mock(Store)
    subject = new ScopedStoreGlobal(store)
  }

  def "delete"() {
    when:
    subject.delete("key1")

    then:
    verify(store).delete("global:key1") || true
  }

  def "get"() {
    when:
    subject.get("key1")

    then:
    verify(store).get("global:key1") || true
  }

  def "put"() {
    when:
    subject.put("key1", "value1", 10)

    then:
    verify(store).put("global:key1", "value1", 10) || true
  }

  def "put without TTL"() {
    when:
    subject.put("key1", "value1")

    then:
    verify(store).put("global:key1", "value1") || true
  }

  def "putIfNotExist"() {
    when:
    when(store.putIfNotExist("global:key1", "value1", 10)).thenReturn(true)
    def result = subject.putIfNotExist("key1", "value1", 10)

    then:
    verify(store).putIfNotExist("global:key1", "value1", 10) || true
    result
  }

  def "putIfNotExist without TTL"() {
    when:
    when(store.putIfNotExist("global:key1", "value1")).thenReturn(true)
    def result = subject.putIfNotExist("key1", "value1")

    then:
    verify(store).putIfNotExist("global:key1", "value1") || true
    result
  }
}