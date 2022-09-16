package com.mx.common.accessors

import com.mx.common.accessors.AccessorConnections
import com.mx.common.collections.ObjectMap
import com.mx.common.connect.AccessorConnectionSettings

import spock.lang.Specification

class AccessorConnectionsTest extends Specification {

  def "builder"() {
    given:
    def subject = new AccessorConnections()
    def connection = AccessorConnectionSettings.builder().build()

    when:
    subject.addConnection("connection_1", connection)

    then:
    subject.getConnection("connection_1") == connection
  }

  def "describe"() {
    given:
    def subject = new AccessorConnections()
    def connection = AccessorConnectionSettings.builder().build()
    subject.addConnection("connection_1", connection)

    when:
    def description = new ObjectMap()
    subject.describe(description)

    then:
    description.get("connection_1") != null
  }
}
