package com.mx.path.connect.http.certificate;

import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Interface for provider that configures and validates mutual authentication settings.
 */
public interface MutualAuthProvider {

  /**
   * Add mutual authentication settings to the given {@link HttpClientBuilder}.
   *
   * @param builder {@link HttpClientBuilder} to which mutual authentication settings should be added
   * @return modified {@link HttpClientBuilder} with mutual authentication configurations applied
   */
  HttpClientBuilder add(HttpClientBuilder builder);

  /**
   * Validates mutual authentication settings.
   *
   * @throws FieldSettingsValidationError to be thrown
   */
  void validate() throws FieldSettingsValidationError;
}
