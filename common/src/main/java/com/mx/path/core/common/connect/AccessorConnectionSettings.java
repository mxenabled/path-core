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

  /**
   * Used to represent this connection's uniqueness for mutual auth
   *
   * <p>Only override if your class needs more uniqueness. (rare)
   * @return Hash of baseUrl, certificateAlias, and keystorePath.
   */
  @SuppressWarnings("MagicNumber")
  @Override
  public int mutualAuthProviderHashcode() {
    int result = 1;

    Object thisBaseUrl = this.getBaseUrl();
    result = result * 59 + (thisBaseUrl == null ? 43 : thisBaseUrl.hashCode());

    Object thisCertificateAlias = this.getCertificateAlias();
    result = result * 59 + (thisCertificateAlias == null ? 43 : thisCertificateAlias.hashCode());

    Object thisKeystorePath = this.getKeystorePath();
    result = result * 59 + (thisKeystorePath == null ? 43 : thisKeystorePath.hashCode());

    return result;
  }
}
