package com.mx.path.core.common.accessor

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.common.connect.AccessorConnectionSettings

import spock.lang.Specification

class AccessorConnectionTest extends Specification {

  def "builder"() {
    when:
    def subject = AccessorConnectionSettings.builder()
        .baseUrl("base_url")
        .certificateAlias("alias")
        .keystorePath("/right/here")
        .configuration("config", "value")
        .keystorePassword("password".toCharArray())
        .skipHostNameVerify(true)
        .build()

    then:
    subject.getBaseUrl() == "base_url"
    subject.getCertificateAlias() == "alias"
    String.valueOf(subject.getKeystorePassword()) == "password"
    subject.getKeystorePath() == "/right/here"
    subject.configurations.get("config") == "value"
    subject.getSkipHostNameVerify()
  }

  def "describe empty"() {
    given:
    def subject = AccessorConnectionSettings.builder().build()

    when:
    def description = new ObjectMap()
    subject.describe(description)

    then:
    description.size() == 0
  }

  def "describe"() {
    given:
    def subject = AccessorConnectionSettings.builder()
        .baseUrl("http://localhost:9090")
        .certificateAlias("cert")
        .configuration("billy", "jean")
        .keystorePassword("password".toCharArray())
        .keystorePath("/right/here")
        .build()

    when:
    def description = new ObjectMap()
    subject.describe(description)

    then:
    description.get("baseUrl") == "http://localhost:9090"
    description.get("certificateAlias") == "cert"
    description.get("keystorePassword") == "********"
    description.get("keystorePath") == "/right/here"
    description.getMap("configurations").get("billy") == "jean"
  }
}
