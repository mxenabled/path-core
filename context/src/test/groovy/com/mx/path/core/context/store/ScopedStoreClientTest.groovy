package com.mx.path.core.context.store

import static org.mockito.Mockito.*

import com.mx.path.core.common.store.Store
import com.mx.path.core.context.GatewayContextException
import com.mx.path.core.context.RequestContext
import com.mx.testing.WithMockery

import spock.lang.Specification

class ScopedStoreClientTest extends Specification implements WithMockery {

  Store store
  ScopedStoreClient subject

  def setup() {
    store = mock(Store)
    subject = new ScopedStoreClient(store)

    RequestContext.builder().clientId("client1").build().register()
  }

  def cleanup() {
    RequestContext.clear()
  }

  def "delete"() {
    when:
    subject.delete("key1")

    then:
    verify(store).delete("client1:key1") || true
  }

  def "get"() {
    when:
    subject.get("key1")

    then:
    verify(store).get("client1:key1") || true
  }

  def "put"() {
    when:
    subject.put("key1", "value1", 10)

    then:
    verify(store).put("client1:key1", "value1", 10) || true
  }

  def "put without TTL"() {
    when:
    subject.put("key1", "value1",)

    then:
    verify(store).put("client1:key1", "value1") || true
  }

  def "putIfNotExist"() {
    when:
    when(store.putIfNotExist("client1:key1", "value1", 10)).thenReturn(true)
    def result = subject.putIfNotExist("key1", "value1", 10)

    then:
    verify(store).putIfNotExist("client1:key1", "value1", 10) || true
    result
  }

  def "putIfNotExist without TTL"() {
    when:
    when(store.putIfNotExist("client1:key1", "value1")).thenReturn(true)
    def result = subject.putIfNotExist("key1", "value1")

    then:
    verify(store).putIfNotExist("client1:key1", "value1") || true
    result
  }

  def "throws exception on null RequestContext"() {
    when:
    RequestContext.clear()
    subject.delete("key1")

    then:
    def e = thrown(GatewayContextException)
    e.message == "Attempting to read clientId from null RequestContext"
  }

  def "throws exception on null RequestContext#clientId"() {
    when:
    RequestContext.builder().build().register()
    subject.delete("key1")

    then:
    def e = thrown(GatewayContextException)
    e.message == "RequestContext#clientId must be non-null to use ScopedStoreClient"
  }
}