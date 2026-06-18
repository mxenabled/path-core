package com.mx.path.testing.session

import com.mx.path.core.common.collection.ObjectMap
import com.mx.path.testing.session.TestEncryptionService

import spock.lang.Specification

class TestEncryptionServiceTest extends Specification {

  TestEncryptionService subject

  def setup() {
    subject = new TestEncryptionService(new ObjectMap())
  }

  def "encrypt returns plaintext unchanged"() {
    expect:
    subject.encrypt("secret") == "secret"
  }

  def "decrypt returns ciphertext unchanged"() {
    expect:
    subject.decrypt("ciphertext") == "ciphertext"
  }

  def "isEncrypted always returns true"() {
    expect:
    subject.isEncrypted("anything")
  }

  def "getConfigurations returns set configurations"() {
    given:
    def configs = new ObjectMap()
    configs.put("key", "value")
    def svc = new TestEncryptionService(configs)

    expect:
    svc.getConfigurations().get("key") == "value"
  }

  def "setConfigurations updates configurations"() {
    given:
    def newConfigs = new ObjectMap()
    newConfigs.put("updated", "true")

    when:
    subject.setConfigurations(newConfigs)

    then:
    subject.getConfigurations().get("updated") == "true"
  }
}
