package com.mx.path.api.connect.http.certificates

import spock.lang.Specification

class LoadedKeystoreTest extends Specification {

  def path = "./src/test/resources/keystore.jks"
  def password = "secret".toCharArray()

  def "loads the keystore"() {
    when:
    def loadedKeystore = LoadedKeystore.load(path, password)

    then:
    loadedKeystore != null
    loadedKeystore == LoadedKeystore.load(path, password)
  }

  def "loads keypair"() {
    given:
    def loadedKeystore = LoadedKeystore.load(path, password)

    when:
    def keypair = loadedKeystore.getKeyPair("test")

    then:
    keypair != null
    keypair == loadedKeystore.getKeyPair("test")
  }
}