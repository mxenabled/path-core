package com.mx.accessors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mx.common.collections.ObjectMap;
import com.mx.common.lang.Strings;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessorConnection implements ConnectionSettings {

  private String baseUrl;
  private String certificateAlias;
  private ObjectMap configurations;
  private char[] keystorePassword;
  private String keystorePath;
  private boolean skipHostNameVerify;

  public static class AccessorConnectionBuilder {

    private ObjectMap configurations = new ObjectMap();

    public final AccessorConnectionBuilder configuration(String key, Object value) {
      configurations.put(key, value);

      return this;
    }

  }

  @Override
  public final boolean getSkipHostNameVerify() {
    return skipHostNameVerify;
  }

  public final void describe(ObjectMap description) {
    if (Strings.isNotBlank(baseUrl)) {
      description.put("baseUrl", baseUrl);
    }

    if (Strings.isNotBlank(certificateAlias)) {
      description.put("certificateAlias", certificateAlias);
    }

    if (keystorePassword != null) {
      description.put("keystorePassword", com.google.common.base.Strings.repeat("*", keystorePassword.length));
    }

    if (Strings.isNotBlank(keystorePath)) {
      description.put("keystorePath", keystorePath);
    }

    if (skipHostNameVerify) {
      description.put("skipHostNameVerify", true);
    }

    if (!configurations.isEmpty()) {
      ObjectMap configs = description.createMap("configurations");
      configurations.forEach(configs::put);
    }
  }

}
