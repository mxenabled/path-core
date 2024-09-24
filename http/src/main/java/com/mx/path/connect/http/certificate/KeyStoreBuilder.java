package com.mx.path.connect.http.certificate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mx.path.core.common.connect.ConnectionSettings;

/**
 * Utility class to load and build keystores.
 */
public class KeyStoreBuilder {

  // Fields

  private String certificateAlias;
  private char[] keystorePassword;
  private String keystorePath;
  private LoadedKeystore keyStore;

  // Constructors

  /**
   * Build new {@link KeyStoreBuilder} using provided {@link ConnectionSettings}.
   *
   * @param settings {@link ConnectionSettings} containing configuration for keystore
   */
  public KeyStoreBuilder(ConnectionSettings settings) {
    this.certificateAlias = settings.getCertificateAlias();
    this.keystorePassword = settings.getKeystorePassword();
    this.keystorePath = settings.getKeystorePath();
    keyStore = LoadedKeystore.load(this.keystorePath, this.keystorePassword);
  }

  /**
   * Return loaded keystore.
   *
   * @return loaded keystore
   */
  public final LoadedKeystore getKeyStore() {
    return keyStore;
  }

  /**
   * Return keystore password.
   *
   * @return keystore password
   */
  public final char[] getKeystorePassword() {
    return keystorePassword.clone();
  }

  /**
   * Return alias to keystore certificate.
   *
   * @return keystore certificate alias
   */
  public final String getCertificateAlias() {
    return certificateAlias;
  }

  /**
   * Return keystore path.
   *
   * @return keystore path
   */
  public final String getKeystorePath() {
    return keystorePath;
  }

  /**
   * Validate current settings for keystore configuration.
   *
   * @throws FieldSettingsValidationError to be thrown
   */
  @SuppressWarnings("PMD.CyclomaticComplexity")
  public final void validateSettings() throws FieldSettingsValidationError {
    List<FieldSettingsValidationError.Field> fieldErrors = new ArrayList<FieldSettingsValidationError.Field>();

    if (keystorePath == null || keystorePath.equals("")) {
      fieldErrors.add(new FieldSettingsValidationError.Field("keystorePath", "must not be empty"));
    } else if (!new File(keystorePath).exists()) {
      fieldErrors.add(new FieldSettingsValidationError.Field("keystorePath", "file does not exist"));
    }

    if (keystorePassword == null || keystorePassword.length == 0) {
      fieldErrors.add(new FieldSettingsValidationError.Field("keystorePassword", "must not be empty"));
    }

    if (certificateAlias == null || certificateAlias.equals("")) {
      fieldErrors.add(new FieldSettingsValidationError.Field("certificateAlias", "must not be empty"));
    }

    if (fieldErrors.size() > 0) {
      throw new FieldSettingsValidationError(fieldErrors);
    }
  }
}
