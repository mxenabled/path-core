package com.mx.path.core.common.connect;

import java.util.List;

import com.mx.path.core.common.collection.ObjectMap;

public interface ConnectionSettings {

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
   * Used to represent this connection's uniqueness for mutual auth
   *
   * <p>Only override if your class needs more uniqueness. (rare)
   * @return Hash of baseUrl, certificateAlias, and keystorePath.
   */
  @SuppressWarnings("MagicNumber")
  default int mutualAuthProviderHashcode() {
    int result = 1;

    Object thisBaseUrl = this.getBaseUrl();
    result = result * 59 + (thisBaseUrl == null ? 43 : thisBaseUrl.hashCode());

    Object thisCertificateAlias = this.getCertificateAlias();
    result = result * 59 + (thisCertificateAlias == null ? 43 : thisCertificateAlias.hashCode());

    Object thisKeystorePath = this.getKeystorePath();
    result = result * 59 + (thisKeystorePath == null ? 43 : thisKeystorePath.hashCode());

    return result;
  }

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
