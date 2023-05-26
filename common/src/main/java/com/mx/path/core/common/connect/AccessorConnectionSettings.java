package com.mx.path.core.common.connect;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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

  private static final int STARTING_HASH_RESULT = 17;

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

  @Override
  public final boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof AccessorConnectionSettings)) {
      return false;
    }
    AccessorConnectionSettings other = (AccessorConnectionSettings) o;
    return Objects.equals(this.getBaseUrl(), other.getBaseUrl())
        && Objects.equals(this.getCertificateAlias(), other.getCertificateAlias())
        && Objects.equals(this.getKeystorePath(), other.getKeystorePath())
        && Arrays.equals(this.getKeystorePassword(), other.getKeystorePassword())
        && Objects.equals(this.getBaseRequestFilters(), other.getBaseRequestFilters());
  }

  @Override
  public final int hashCode() {
    int result = 1;
    final int oddPrimeBase = 31;
    if (getBaseUrl() != null) {
      result = oddPrimeBase * STARTING_HASH_RESULT + getBaseUrl().hashCode();
    }
    if (getCertificateAlias() != null) {
      result = oddPrimeBase * result + getCertificateAlias().hashCode();
    }
    if (getKeystorePath() != null) {
      result = oddPrimeBase * result + getKeystorePath().hashCode();
    }
    if (getKeystorePassword() != null) {
      result = oddPrimeBase * result + Arrays.hashCode(getKeystorePassword());
    }
    if (getBaseRequestFilters() != null) {
      result = oddPrimeBase * result + getBaseRequestFilters().hashCode();
    }
    return result;
  }
}
