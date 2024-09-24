package com.mx.path.core.common.connect;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.lang.Strings;

/**
 * Implementation of ConnectionSettings.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessorConnectionSettings implements ConnectionSettings {

  /**
   * Request filters.
   *
   * -- GETTER --
   * Return request filters.
   *
   * @return request filters
   * -- SETTER --
   * Set request filters.
   *
   * @param baseRequestFilters request filters to set
   */
  private List<RequestFilter> baseRequestFilters;

  /**
   * Base url for requests.
   *
   * -- GETTER --
   * Return base url.
   *
   * @return base url
   * -- SETTER --
   * Set base url.
   *
   * @param baseUrl base url to set
   */
  private String baseUrl;

  /**
   * Request certificate alias.
   *
   * -- GETTER --
   * Return certificate alias.
   *
   * @return request certificate alias
   * -- SETTER --
   * Set request certificate alias.
   *
   * @param certificateAlias request certificate alias to set
   */
  private String certificateAlias;

  /**
   * Request configurations.
   *
   * -- GETTER --
   * Return request configurations.
   *
   * @return request configurations
   * -- SETTER --
   * Set request configurations.
   *
   * @param configurations request configurations to set
   */
  private ObjectMap configurations;

  /**
   * Connect timeout limit.
   *
   * -- GETTER --
   * Return connect timeout limit.
   *
   * @return connect timeout limit
   * -- SETTER --
   * Set connect timeout limit.
   *
   * @param connectTimeout connect timeout limit to set
   */
  private Duration connectTimeout;

  /**
   * Request keystore passwords.
   *
   * -- GETTER --
   * Return request keystore passwords.
   *
   * @return request keystore passwords
   * -- SETTER --
   * Set request keystore passwords.
   *
   * @param keystorePassword request keystore passwords to set
   */
  private char[] keystorePassword;

  /**
   * Request keystore path.
   *
   * -- GETTER --
   * Return request keystore path.
   *
   * @return request keystore path
   * -- SETTER --
   * Set request keystore path.
   *
   * @param keystorePath request keystore path to set
   */
  private String keystorePath;

  /**
   * Request timeout limit.
   *
   * -- GETTER --
   * Return request timeout limit.
   *
   * @return request timeout limit
   * -- SETTER --
   * Set request timeout limit.
   *
   * @param requestTimeout request timeout limit to set
   */
  private Duration requestTimeout;

  /**
   * Should skip host name verification.
   *
   * -- GETTER --
   * Return should skip host name verification.
   *
   * @return should skip host name verification
   * -- SETTER --
   * Set should skip host name verification.
   *
   * @param skipHostNameVerify should skip hostname verification
   */
  private boolean skipHostNameVerify;

  /**
   * Utility builder class for {@link AccessorConnectionSettings}.
   */
  public static class AccessorConnectionSettingsBuilder {

    private ObjectMap configurations = new ObjectMap();
    private List<RequestFilter> baseRequestFilters = new LinkedList<RequestFilter>();

    /**
     * Append new configuration.
     *
     * @param key configuration key
     * @param value configuration value
     * @return self
     */
    public final AccessorConnectionSettingsBuilder configuration(String key, Object value) {
      configurations.put(key, value);

      return this;
    }

    /**
     * Append new filter.
     *
     * @param requestFilter filter to append
     * @return self
     */
    public final AccessorConnectionSettingsBuilder baseRequestFilter(RequestFilter requestFilter) {
      baseRequestFilters.add(requestFilter);

      return this;
    }

  }

  /**
   * Return connection timeout limit.
   *
   * @return connection timeout limit
   */
  @Override
  public final Duration getConnectTimeout() {
    return connectTimeout;
  }

  /**
   * Return request timeout limit.
   *
   * @return request timeout limit
   */
  @Override
  public final Duration getRequestTimeout() {
    return requestTimeout;
  }

  /**
   * Return should skip name verify.
   *
   * @return should skip
   */
  @Override
  public final boolean getSkipHostNameVerify() {
    return skipHostNameVerify;
  }

  /**
   * Return object description.
   *
   * @param description description
   */
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
