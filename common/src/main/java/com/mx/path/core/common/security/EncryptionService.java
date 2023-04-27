package com.mx.path.core.common.security;

/**
 * Interface for encryption service provider
 */
public interface EncryptionService {

  /**
   * Encrypt a string
   * @param value
   * @return
   */
  String encrypt(String value);

  /**
   * Decrypt a string
   * @param cypher
   * @return
   */
  String decrypt(String cypher);

  /**
   * Determine whether a given string has already been encrypted
   * @param value
   * @return
   */
  boolean isEncrypted(String value);

  /**
   * Performs a rotation on one or more keys
   */
  default void rotateKeys() {
  }
}
