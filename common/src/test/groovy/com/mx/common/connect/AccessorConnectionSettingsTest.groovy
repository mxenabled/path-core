package com.mx.common.connect

import com.mx.path.core.common.connect.AccessorConnectionSettings

import spock.lang.Specification

class AccessorConnectionSettingsTest extends Specification {

  def "equals and hashcode"() {
    given:
    def subject = new AccessorConnectionSettings().tap {
      setBaseUrl("https://hughesfcu.architect-cert.fiservapps.com/")
      setCertificateAlias("hughes")
      setKeystorePath("/path/to/keystore")
      setKeystorePassword("pa\$\$w0rd".toCharArray())
    }
    def other = new AccessorConnectionSettings().tap {
      setBaseUrl("https://hughesfcu.architect-cert.fiservapps.com/")
      setCertificateAlias("hughes")
      setKeystorePath("/path/to/keystore")
      setKeystorePassword("pa\$\$w0rd".toCharArray())
    }

    expect:
    subject == other
    subject.hashCode() == other.hashCode()
  }
}
