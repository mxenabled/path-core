package com.mx.testing;

import lombok.Getter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.security.EncryptionService;
import com.mx.path.core.context.Session;

public class TestEncryptionService implements EncryptionService {
  @Getter
  ObjectMap configurations;

  public static void register() {
    EncryptionService encryptionService = new TestEncryptionService(new ObjectMap());
    Session.setEncryptionServiceSupplier(() -> encryptionService);
  }

  public TestEncryptionService(ObjectMap configurations) {
    this.configurations = configurations;
  }

  @Override
  public String encrypt(String plaintext) {
    return plaintext;
  }

  @Override
  public String decrypt(String ciphertext) {
    return ciphertext;
  }

  @Override
  public boolean isEncrypted(String text) {
    return true;
  }
}
