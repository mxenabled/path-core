package com.mx.path.gateway.security;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mx.common.connect.ConnectionSettings;

/**
 * @deprecated moved to com.mx.path.api.connect.http
 */
@Deprecated
public class KeyStoreBuilder {

  // Fields

  private String certificateAlias;
  private char[] keystorePassword;
  private String keystorePath;
  private LoadedKeystore keyStore;

  // Constructors

  public KeyStoreBuilder(ConnectionSettings settings) {
    this.certificateAlias = settings.getCertificateAlias();
    this.keystorePassword = settings.getKeystorePassword();
    this.keystorePath = settings.getKeystorePath();
    keyStore = LoadedKeystore.load(this.keystorePath, this.keystorePassword);
  }

  public final LoadedKeystore getKeyStore() {
    return keyStore;
  }

  public final char[] getKeystorePassword() {
    return keystorePassword.clone();
  }

  public final String getCertificateAlias() {
    return certificateAlias;
  }

  public final String getKeystorePath() {
    return keystorePath;
  }

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
