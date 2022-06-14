package com.mx.accessors

import com.mx.common.collections.ObjectMap

import spock.lang.Specification

class AccessorConnectionsTest extends Specification {

  def "builder"() {
    given:
    def subject = new AccessorConnections()
    def connection = AccessorConnection.builder().build()

    when:
    subject.addConnection("connection_1", connection)

    then:
    subject.getConnection("connection_1") == connection
  }

  def "describe"() {
    given:
    def subject = new AccessorConnections()
    def connection = AccessorConnection.builder().build()
    subject.addConnection("connection_1", connection)

    when:
    def description = new ObjectMap()
    subject.describe(description)

    then:
    description.get("connection_1") != null
  }
}
