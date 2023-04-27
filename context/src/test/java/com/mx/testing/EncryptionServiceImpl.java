package com.mx.testing;

import lombok.Getter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.security.EncryptionService;

public class EncryptionServiceImpl implements EncryptionService {
  @Getter
  private final ObjectMap configurations;

  public EncryptionServiceImpl(ObjectMap configurations) {
    this.configurations = configurations;
  }

  @Override
  public String encrypt(String value) {
    return value;
  }

  @Override
  public String decrypt(String cypher) {
    return cypher;
  }

  @Override
  public boolean isEncrypted(String value) {
    return true;
  }
}
