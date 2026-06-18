package com.mx.path.api.connect.http.certificate

import com.mx.path.connect.http.certificate.FieldSettingsValidationError
import com.mx.path.connect.http.certificate.KeyStoreBuilder
import com.mx.path.core.common.connect.AccessorConnectionSettings

import spock.lang.Specification

class KeyStoreBuilderTest extends Specification {

  static final String KEYSTORE_PATH = "./src/test/resources/keystore.jks"
  static final char[] KEYSTORE_PASSWORD = "secret".toCharArray()
  static final String CERT_ALIAS = "test"

  def buildValidSettings() {
    def s = new AccessorConnectionSettings()
    s.setKeystorePath(KEYSTORE_PATH)
    s.setKeystorePassword(KEYSTORE_PASSWORD)
    s.setCertificateAlias(CERT_ALIAS)
    return s
  }

  def "constructor exposes all settings"() {
    given:
    def builder = new KeyStoreBuilder(buildValidSettings())

    expect:
    builder.getKeystorePath() == KEYSTORE_PATH
    String.valueOf(builder.getKeystorePassword()) == "secret"
    builder.getCertificateAlias() == CERT_ALIAS
    builder.getKeyStore() != null
  }

  def "validateSettings passes when all fields are valid"() {
    given:
    def builder = new KeyStoreBuilder(buildValidSettings())

    when:
    builder.validateSettings()

    then:
    noExceptionThrown()
  }

  def "validateSettings throws when certificateAlias is null"() {
    given:
    def builder = new KeyStoreBuilder(buildValidSettings())
    def f = KeyStoreBuilder.getDeclaredField("certificateAlias")
    f.setAccessible(true)
    f.set(builder, null)

    when:
    builder.validateSettings()

    then:
    def ex = thrown(FieldSettingsValidationError)
    ex.getFields().any { it.getField() == "certificateAlias" }
  }

  def "validateSettings throws when certificateAlias is empty"() {
    given:
    def builder = new KeyStoreBuilder(buildValidSettings())
    def f = KeyStoreBuilder.getDeclaredField("certificateAlias")
    f.setAccessible(true)
    f.set(builder, "")

    when:
    builder.validateSettings()

    then:
    def ex = thrown(FieldSettingsValidationError)
    ex.getFields().any { it.getField() == "certificateAlias" }
  }

  def "validateSettings throws when keystorePassword is null"() {
    given:
    def builder = new KeyStoreBuilder(buildValidSettings())
    def f = KeyStoreBuilder.getDeclaredField("keystorePassword")
    f.setAccessible(true)
    f.set(builder, null)

    when:
    builder.validateSettings()

    then:
    def ex = thrown(FieldSettingsValidationError)
    ex.getFields().any { it.getField() == "keystorePassword" }
  }

  def "validateSettings throws when keystorePassword is empty array"() {
    given:
    def builder = new KeyStoreBuilder(buildValidSettings())
    def f = KeyStoreBuilder.getDeclaredField("keystorePassword")
    f.setAccessible(true)
    f.set(builder, new char[0])

    when:
    builder.validateSettings()

    then:
    def ex = thrown(FieldSettingsValidationError)
    ex.getFields().any { it.getField() == "keystorePassword" }
  }

  def "validateSettings throws when keystorePath points to non-existent file"() {
    given:
    def builder = new KeyStoreBuilder(buildValidSettings())
    def f = KeyStoreBuilder.getDeclaredField("keystorePath")
    f.setAccessible(true)
    f.set(builder, "/no/such/file.jks")

    when:
    builder.validateSettings()

    then:
    def ex = thrown(FieldSettingsValidationError)
    ex.getFields().any { it.getField() == "keystorePath" }
  }

  def "validateSettings throws when keystorePath is null"() {
    given:
    def builder = new KeyStoreBuilder(buildValidSettings())
    def f = KeyStoreBuilder.getDeclaredField("keystorePath")
    f.setAccessible(true)
    f.set(builder, null)

    when:
    builder.validateSettings()

    then:
    def ex = thrown(FieldSettingsValidationError)
    ex.getFields().any { it.getField() == "keystorePath" }
  }
}
