package com.mx.path.core.context.store

import com.mx.testing.StoreImpl

import spock.lang.Specification

class ScopedStoreTest extends Specification {

  def "build"() {
    given:
    def store = new StoreImpl()

    when:
    def result = ScopedStore.build(store, "global")

    then: "global"
    result.class == ScopedStoreGlobal

    when:
    result = ScopedStore.build(store, "client")

    then: "client"
    result.class == ScopedStoreClient

    when:
    result = ScopedStore.build(store, "user")

    then: "user"
    result.class == ScopedStoreCurrentUser

    when:
    result = ScopedStore.build(store, "session")

    then: "session"
    result.class == ScopedStoreCurrentSession

    when:
    result = ScopedStore.build(store, "junk")

    then: "default"
    result.class == ScopedStoreCurrentSession
  }
}