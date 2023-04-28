package com.mx.path.connect.http.certificate;

import org.apache.http.impl.client.HttpClientBuilder;

public interface MutualAuthProvider {
  HttpClientBuilder add(HttpClientBuilder builder);

  void validate() throws FieldSettingsValidationError;
}
