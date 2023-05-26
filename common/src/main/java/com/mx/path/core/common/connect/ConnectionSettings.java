package com.mx.path.core.common.connect;

import java.util.List;

import com.mx.path.core.common.collection.ObjectMap;

public interface ConnectionSettings {

  int STARTING_HASH_RESULT = 17;

  /**
   * @return connection's base URL
   */
  String getBaseUrl();

  /**
   * @return alias for client certificate to be used by connection
   */
  String getCertificateAlias();

  /**
   * @return path to keystore used to store certificates
   */
  String getKeystorePath();

  /**
   * @return password used to access certificates in keystore
   */
  char[] getKeystorePassword();

  /**
   * @return list of configured request filters to be used when executing connection's requests
   */
  List<RequestFilter> getBaseRequestFilters();

  /**
   * @return true, if host name should be checked against the certificate
   */
  boolean getSkipHostNameVerify();

  default void describe(ObjectMap description) {
  }
}
