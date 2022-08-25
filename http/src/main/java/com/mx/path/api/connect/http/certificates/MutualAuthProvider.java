package com.mx.path.api.connect.http.certificates;

import org.apache.http.impl.client.HttpClientBuilder;

public interface MutualAuthProvider {
  HttpClientBuilder add(HttpClientBuilder builder);

  void validate() throws FieldSettingsValidationError;
}
