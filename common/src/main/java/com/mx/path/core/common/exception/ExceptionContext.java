package com.mx.path.core.common.exception;

import java.util.Map;

import javax.annotation.Nullable;

import com.mx.path.core.common.collection.MultiValueMap;

/**
 * Interface that provides contextual information about around exception.
 */
public interface ExceptionContext {
  /**
   * Additional (arbitrary) contextual information.
   *
   * @return map of contextual data
   */
  @Nullable
  Map<String, String> getContext();

  /**
   * Return client ID.
   *
   * @return client ID
   */
  @Nullable
  default String getClientId() {
    // todo: remove default on next major version bump
    return "";
  }

  /**
   * @return The environment in which the exception was thrown. (example: sandbox, qa, production)
   */
  default String getEnvironment() {
    // todo: Remove default on next major version
    return "development";
  }

  /**
   * @return the feature associated with request
   */
  default String getFeature() {
    // todo: remove default on next major version bump
    return null;
  }

  /**
   * @return incoming request headers
   */
  @Nullable
  MultiValueMap<String, String> getHeaders();

  /**
   * @return incoming request parameters
   */
  @Nullable
  MultiValueMap<String, String> getParameters();

  /**
   * @return a trace id associated with the path session
   */
  default String getSessionTraceId() {
    return null;
  }

  /**
   * @return incoming request http method
   */
  @Nullable
  String getMethod();

  /**
   * @return incoming request path info
   */
  @Nullable
  String getPathInfo();

  /**
   * @return incoming request translated path info
   */
  @Nullable
  String getPathTranslated();

  /**
   * @return incoming request query string
   */
  @Nullable
  String getQueryString();

  /**
   * @return incoming request remote IP address
   */
  @Nullable
  String getRemoteAddr();

  /**
   * @return incoming request remote port
   */
  int getRemotePort();

  /**
   * @return incoming request url
   */
  @Nullable
  String getRequestURL();

  /**
   * @return receiving server name
   */
  @Nullable
  String getServerName();

  /**
   * @return receiving server port
   */
  int getServerPort();

  /**
   * @return incoming request protocol
   */
  @Nullable
  String getServerProtocol();

  /**
   * @return session create time in Epoch seconds
   */
  long getSessionCreateTime();

  /**
   * @return session id
   */
  @Nullable
  String getSessionId();

  /**
   * @return request trace id
   */
  @Nullable
  String getTraceId();

  /**
   * @return request user id
   */
  @Nullable
  default String getUserId() {
    // todo: remove default on next major version bump
    return null;
  }
}
