package com.mx.path.testing.session;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.security.EncryptionService;

/**
 * Utility class for test of encryption services.
 */
public class TestEncryptionService implements EncryptionService {

  /**
   * -- GETTER --
   * Return configurations associated with this class.
   *
   * @return class configurations
   * -- SETTER --
   * Set configurations for this class.
   *
   * @param configurations to set
   */
  @Getter
  @Setter
  private ObjectMap configurations;

  /**
   * Build new test encryption service instance and set configurations.
   *
   * @param configurations to set
   */
  public TestEncryptionService(ObjectMap configurations) {
    this.configurations = configurations;
  }

  @Override
  public final String encrypt(String plaintext) {
    return plaintext;
  }

  @Override
  public final String decrypt(String ciphertext) {
    return ciphertext;
  }

  @Override
  public final boolean isEncrypted(String text) {
    return true;
  }
}
