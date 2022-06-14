package com.mx.accessors;

public interface ConnectionSettings {
  default String getBaseUrl() {
    return null;
  }

  String getCertificateAlias();

  String getKeystorePath();

  char[] getKeystorePassword();

  default boolean getSkipHostNameVerify() {
    return false;
  }
}
