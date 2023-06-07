package com.mx.common.connect

import com.mx.common.collections.ObjectMap

import spock.lang.Specification

class AccessorConnectionSettingsTest extends Specification {
  AccessorConnectionSettings settings

  def setup() {
    settings = new AccessorConnectionSettings()
  }

  def "test mutualAuthProviderHashcode"() {
    given:
    settings.setBaseUrl("http://localhost:3001")
    settings.setCertificateAlias("certificate1")
    settings.setKeystorePath("./src/test/resources/keystore.jks")

    when:
    def first = settings.mutualAuthProviderHashcode()

    then:
    first == settings.mutualAuthProviderHashcode()

    when:
    settings.setBaseUrl("http://localhost:3002")
    settings.setCertificateAlias("certificate1")
    settings.setKeystorePath("./src/test/resources/keystore.jks")

    then:
    first != settings.mutualAuthProviderHashcode()

    when:
    settings.setBaseUrl("http://localhost:3001")
    settings.setCertificateAlias("certificate2")
    settings.setKeystorePath("./src/test/resources/keystore.jks")

    then:
    first != settings.mutualAuthProviderHashcode()

    when:
    settings.setBaseUrl("http://localhost:3001")
    settings.setCertificateAlias("certificate1")
    settings.setKeystorePath("./src/test/resources/another_keystore.jks")

    then:
    first != settings.mutualAuthProviderHashcode()

    when:
    settings.setBaseUrl("http://localhost:3001")
    settings.setCertificateAlias("certificate1")
    settings.setKeystorePath("./src/test/resources/keystore.jks")

    // Does not care about these properties
    settings.setKeystorePassword("bobIsYourUncle".toCharArray())
    settings.setSkipHostNameVerify(true)
    settings.setConfigurations(new ObjectMap().tap { put("uncle", "bob") })

    then:
    first == settings.mutualAuthProviderHashcode()
  }
}
