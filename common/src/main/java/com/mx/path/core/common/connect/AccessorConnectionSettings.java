package com.mx.path.core.common.connect;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.lang.Strings;

/**
 * Implementation of ConnectionSettings
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessorConnectionSettings implements ConnectionSettings {

  private String baseUrl;
  private String certificateAlias;
  private ObjectMap configurations;
  private char[] keystorePassword;
  private String keystorePath;
  private List<RequestFilter> baseRequestFilters;
  private boolean skipHostNameVerify;

  public static class AccessorConnectionSettingsBuilder {

    private ObjectMap configurations = new ObjectMap();
    private List<RequestFilter> baseRequestFilters = new LinkedList<RequestFilter>();

    public final AccessorConnectionSettingsBuilder configuration(String key, Object value) {
      configurations.put(key, value);

      return this;
    }

    public final AccessorConnectionSettingsBuilder baseRequestFilter(RequestFilter requestFilter) {
      baseRequestFilters.add(requestFilter);

      return this;
    }

  }

  @Override
  public final boolean getSkipHostNameVerify() {
    return skipHostNameVerify;
  }

  @Override
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
