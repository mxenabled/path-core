package com.mx.path.testing.session;

import lombok.Getter;
import lombok.Setter;

import com.mx.common.collections.ObjectMap;
import com.mx.common.security.EncryptionService;

public class TestEncryptionService implements EncryptionService {
  @Getter
  @Setter
  private ObjectMap configurations;

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
