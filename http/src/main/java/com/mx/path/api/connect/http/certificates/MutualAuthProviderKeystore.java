package com.mx.path.api.connect.http.certificates;

import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;

import com.mx.common.connect.ConnectionSettings;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * Provides SslContext given a Java Keystore
 */
public class MutualAuthProviderKeystore implements MutualAuthProvider {

  private KeyStoreBuilder keyStoreBuilder;

  // MutualAuthProvider

  public MutualAuthProviderKeystore(ConnectionSettings settings) {
    this.keyStoreBuilder = new KeyStoreBuilder(settings);
  }

  /**
   * Add keystores to webclient.
   * <p>
   * For more info, see https://stackoverflow.com/questions/45418523/spring-5-webclient-using-ssl
   *
   * @param builder
   * @return Builder
   */
  @Override
  public final HttpClientBuilder add(HttpClientBuilder builder) {
    try {
      SSLContextBuilder context = SSLContextBuilder.create().loadKeyMaterial(keyStoreBuilder.getKeyStore().getKeyStore(), keyStoreBuilder.getKeystorePassword(), new PrivateKeyStrategy() {
        @Override
        public String chooseAlias(Map<String, PrivateKeyDetails> map, Socket socket) {
          return keyStoreBuilder.getCertificateAlias();
        }
      });

      return builder.setSSLContext(context.build());
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
      throw new RuntimeException("Unable to load key with alias " + keyStoreBuilder.getCertificateAlias());
    }
  }

  @Override
  public final void validate() throws FieldSettingsValidationError {
    keyStoreBuilder.validateSettings();
  }
}
