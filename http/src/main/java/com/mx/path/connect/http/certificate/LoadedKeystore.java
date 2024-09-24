package com.mx.path.connect.http.certificate;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import com.mx.path.core.common.lang.Strings;

/**
 * Represents a keystore that has been loaded from a file and is ready for use.
 * This class encapsulates the keystore data and associated metadata.
 */
public final class LoadedKeystore {

  /**
   * Represents a pair of cryptographic keys consisting of a public key and a private key,
   * along with an associated certificate.
   */
  public static class KeyPair {
    private X509Certificate certificate;
    private PrivateKey key;
    private PublicKey publicKey;

    KeyPair(X509Certificate certificate, PrivateKey key, PublicKey publicKey) {
      this.key = key;
      this.publicKey = publicKey;
      this.certificate = certificate;
    }

    /**
     * Return private key.
     *
     * @return {@link PrivateKey}
     */
    public final PrivateKey getKey() {
      return key;
    }

    /**
     * Return certificate.
     *
     * @return {@link X509Certificate}
     */
    public final X509Certificate getCertificate() {
      return certificate;
    }

    /**
     * Return public key.
     *
     * @return {@link PublicKey}
     */
    public final PublicKey getPublicKey() {
      return publicKey;
    }
  }

  private static Map<String, LoadedKeystore> loadedKeyStores = new HashMap<>();

  private String path;
  private KeyStore keyStore;
  private char[] password;
  private Map<String, KeyPair> keyPairs = new HashMap<>();

  /**
   * Load {@link LoadedKeystore} from specified file path using provided password.
   *
   * @param path file path to the keystore
   * @param password password used to access the keystore
   * @return {@link LoadedKeystore} instance loaded from specified path
   */
  public static LoadedKeystore load(String path, char[] password) {
    if (Strings.isBlank(path)) {
      throw new RuntimeException("Keystore path cannot be blank");
    }

    if (!loadedKeyStores.containsKey(path)) {
      synchronized (KeyStoreBuilder.class) {
        if (!loadedKeyStores.containsKey(path)) {
          try {
            KeyStore keyStore = KeyStore.getInstance("jks");
            try (FileInputStream keyStoreFile = new FileInputStream(Paths.get(path).toFile())) {
              keyStore.load(keyStoreFile, password);
              loadedKeyStores.put(path, new LoadedKeystore(path, keyStore, password));
            }
          } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }

    return loadedKeyStores.get(path);
  }

  /**
   * Return key store path.
   *
   * @return path
   */
  public String getPath() {
    return path;
  }

  /**
   * Return key store.
   *
   * @return {@link KeyStore}
   */
  public KeyStore getKeyStore() {
    return keyStore;
  }

  private LoadedKeystore(String path, KeyStore keyStore, char[] password) {
    this.path = path;
    this.keyStore = keyStore;
    this.password = password;
  }

  /**
   * Retrieve {@link KeyPair} associated with the specified alias.
   *
   * @param alias alias associated with key pair to be retrieved
   * @return {@link KeyPair} associated with specified alias
   */
  public KeyPair getKeyPair(String alias) {
    if (!keyPairs.containsKey(alias)) {
      synchronized (this) {
        if (!keyPairs.containsKey(alias)) {
          PrivateKey privateKey = getPrivateKey(alias);
          X509Certificate certificate = getCertificate(alias);
          PublicKey publicKey = getPublicKey(alias);

          keyPairs.put(alias, new KeyPair(certificate, privateKey, publicKey));
        }
      }
    }

    return keyPairs.get(alias);
  }

  private PrivateKey getPrivateKey(String certificateAlias) {
    try {
      return (PrivateKey) this.keyStore.getKey(certificateAlias, this.password);
    } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
      throw new RuntimeException(e);
    }
  }

  private X509Certificate getCertificate(String certificateAlias) {
    try {
      return (X509Certificate) keyStore.getCertificate(certificateAlias);
    } catch (KeyStoreException e) {
      throw new RuntimeException(e);
    }
  }

  private PublicKey getPublicKey(String certificateAlias) {
    try {
      return keyStore.getCertificate(certificateAlias).getPublicKey();
    } catch (KeyStoreException e) {
      throw new RuntimeException(e);
    }
  }
}
