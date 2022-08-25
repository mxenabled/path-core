package com.mx.path.gateway.security;

import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @deprecated moved to com.mx.path.api.connect.http
 */
@Deprecated
public interface MutualAuthProvider {
  HttpClientBuilder add(HttpClientBuilder builder);

  void validate() throws FieldSettingsValidationError;
}
