package com.mx.path.api.connect.http.certificates

import com.mx.path.connect.http.certificates.FieldSettingsValidationError
import com.mx.path.connect.http.certificates.MutualAuthProviderFactory
import com.mx.path.connect.http.certificates.MutualAuthProviderKeystore
import com.mx.path.core.common.connect.ConnectionSettings
import com.mx.path.core.common.connect.RequestFilter

import spock.lang.Specification

class MutualAuthProviderFactoryTest extends Specification {
  private TestMutualAuthSettings settings

  def setup() {
    settings = new TestMutualAuthSettings()
  }

  class TestMutualAuthSettings implements ConnectionSettings {
    private String keystorePath
    private char[] keystorePassword
    private String certificateAlias

    @Override
    final String getKeystorePath() {
      return keystorePath
    }

    final void setKeystorePath(String keystorePath) {
      this.keystorePath = keystorePath
    }

    @Override
    final char[] getKeystorePassword() {
      return keystorePassword
    }

    @Override
    List<RequestFilter> getBaseRequestFilters() {
      return null
    }

    @Override
    boolean getSkipHostNameVerify() {
      return false
    }

    final void setKeystorePassword(char[] keystorePassword) {
      this.keystorePassword = keystorePassword
    }

    @Override
    String getBaseUrl() {
      return null
    }

    @Override
    final String getCertificateAlias() {
      return certificateAlias
    }

    final void setCertificateAlias(String certificateAlias) {
      this.certificateAlias = certificateAlias
    }
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
