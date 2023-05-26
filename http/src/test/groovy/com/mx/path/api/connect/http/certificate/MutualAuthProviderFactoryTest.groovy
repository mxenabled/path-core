package com.mx.path.api.connect.http.certificate

import com.mx.path.connect.http.certificate.FieldSettingsValidationError
import com.mx.path.connect.http.certificate.MutualAuthProviderFactory
import com.mx.path.connect.http.certificate.MutualAuthProviderKeystore
import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.core.common.connect.AccessorConnectionSettings
import com.mx.path.core.common.connect.ConnectionSettings
import com.mx.path.core.common.connect.RequestFilter

import spock.lang.Specification

class MutualAuthProviderFactoryTest extends Specification {
  private TestMutualAuthSettings settings

  def setup() {
    settings = new TestMutualAuthSettings()
  }

  def cleanup() {
    MutualAuthProviderFactory.reset()
  }

  class TestMutualAuthSettings extends AccessorConnectionSettings {
  }

  def "buildGivesNullIfNoSettings"() {
    expect:
    MutualAuthProviderFactory.build(settings) == null
  }

  def "buildsKeystoreProvider"() {
    given:
    settings.setKeystorePath("./src/test/resources/keystore.jks")
    settings.setKeystorePassword("secret".toCharArray())
    settings.setCertificateAlias("test")

    expect:
    MutualAuthProviderFactory.build(settings) instanceof MutualAuthProviderKeystore
  }

  def "mutualAuthEnabledFalseWhenNoSettings"() {
    expect:
    !MutualAuthProviderFactory.isMutualAuthEnabled(settings)
  }

  def "caches mutualAuth according to settings"() {
    given:
    settings.setBaseUrl("http://localhost:3001")
    settings.setCertificateAlias("certificate1")
    settings.setKeystorePath("./src/test/resources/keystore.jks")

    when:
    def first = MutualAuthProviderFactory.build(settings)

    then:
    first === MutualAuthProviderFactory.build(settings)

    when:
    settings.setBaseUrl("http://localhost:3002")
    settings.setCertificateAlias("certificate1")
    settings.setKeystorePath("./src/test/resources/keystore.jks")

    then:
    first !== MutualAuthProviderFactory.build(settings)

    when:
    settings.setBaseUrl("http://localhost:3001")
    settings.setCertificateAlias("certificate2")
    settings.setKeystorePath("./src/test/resources/keystore.jks")

    then:
    first !== MutualAuthProviderFactory.build(settings)

    when:
    settings.setBaseUrl("http://localhost:3001")
    settings.setCertificateAlias("certificate1")
    settings.setKeystorePath("./src/test/resources/another_keystore.jks")

    then:
    first !== MutualAuthProviderFactory.build(settings)

    when:
    settings.setBaseUrl("http://localhost:3001")
    settings.setCertificateAlias("certificate1")
    settings.setKeystorePath("./src/test/resources/keystore.jks")

    // Does not care about these properties
    settings.setKeystorePassword("bobIsYourUncle".toCharArray())
    settings.setSkipHostNameVerify(true)
    settings.setConfigurations(new ObjectMap().tap { put("uncle", "bob") })

    then:
    first === MutualAuthProviderFactory.build(settings)
  }

  def "mutualAuthEnabledTrueWhenSettingsPresent"() {
    when:
    settings.setCertificateAlias("thisOne")

    then:
    MutualAuthProviderFactory.isMutualAuthEnabled(settings)

    when:
    settings = new TestMutualAuthSettings()
    settings.setKeystorePassword("p@\$\$w0r#".toCharArray())

    then:
    MutualAuthProviderFactory.isMutualAuthEnabled(settings)

    when:
    settings = new TestMutualAuthSettings()
    settings.setKeystorePath("/keys_are_here")

    then:
    MutualAuthProviderFactory.isMutualAuthEnabled(settings)
  }

  def "buildValidatesKeystoreProviderMissingCertificationAlias"() {
    given:
    settings.setKeystorePath("./src/test/resources/keystore.jks")
    settings.setKeystorePassword("secret".toCharArray())
    settings.setCertificateAlias(null)

    when:
    MutualAuthProviderFactory.validateSettings(settings)

    then:
    thrown(FieldSettingsValidationError.class)
  }

  def "buildValidatesKeystoreProviderMissingKeystorePassword"() {
    given:
    settings.setKeystorePath("./src/test/resources/keystore.jks")
    settings.setKeystorePassword(null)
    settings.setCertificateAlias("bristow")

    when:
    MutualAuthProviderFactory.validateSettings(settings)

    then:
    thrown(FieldSettingsValidationError.class)
  }

  def "buildValidatesKeystoreProviderMissingKeystorePath"() {
    given:
    settings.setKeystorePath(null)
    settings.setKeystorePassword("secret".toCharArray())
    settings.setCertificateAlias("bristow")

    when:
    MutualAuthProviderFactory.validateSettings(settings)

    then:
    thrown(RuntimeException.class)
  }
}
