package com.mx.path.testing.request;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.mx.path.core.common.connect.Request;

/**
 * Allows rules to be defined for matching incoming requests
 *
 * <p>Examples:
 *
 * <pre>{@code
 *   # Matches any request where request.getPath() == "account"
 *   RequestMatcher.withPath("accounts")
 *
 *   # Matches any request where request.getPath() == "accounts"
 *   #   AND request.getMethod() == "GET"
 *   RequestMatcher.withPath("accounts").withMethod("GET")
 *
 *   # Matches any request where request.equals(exactMatchRequest)
 *   RequestMatcher.withPath(exactMatchRequest)
 * }</pre>
 */
public class RequestMatcher {
  private final List<Function<Request<?, ?>, Boolean>> assertions = new ArrayList<>();
  private final List<String> descriptions = new ArrayList<>();

  /**
   * Compare request with all defined matchers.
   *
   * @param request to compare
   * @return false if any is not matched
   */
  public final boolean isMatch(Request<?, ?> request) {
    return assertions.stream().allMatch((c) -> c.apply(request));
  }

  /**
   * Create and add new matcher for base url field.
   *
   * @param baseUrl to match
   * @return self
   */
  public final RequestMatcher withBaseUrl(String baseUrl) {
    descriptions.add("baseUrl == " + baseUrl);
    assertions.add((request) -> baseUrl.equals(request.getBaseUrl()));
    return this;
  }

  /**
   * Create and add new matcher for method field.
   *
   * @param method to match
   * @return self
   */
  public final RequestMatcher withMethod(String method) {
    descriptions.add("method == " + method);
    assertions.add((request) -> method.equals(request.getMethod()));
    return this;
  }

  /**
   * Create and add new matcher for path field.
   *
   * @param path to match
   * @return self
   */
  public final RequestMatcher withPath(String path) {
    descriptions.add("path == " + path);
    assertions.add((request) -> path.equals(request.getPath()));
    return this;
  }

  /**
   * Create and add new function matcher.
   *
   * @param check new matcher function
   * @return self
   */
  public final RequestMatcher with(Function<Request<?, ?>, Boolean> check) {
    descriptions.add("with check");
    assertions.add(check);
    return this;
  }

  /**
   * Create and add new function matcher.
   *
   * @param check new matcher function
   * @return self
   */
  public final RequestMatcher withMatcher(Function<Request<?, ?>, Boolean> check) {
    descriptions.add("with matcher");
    assertions.add(check);
    return this;
  }

  /**
   * Create and add new request matcher for given request.
   *
   * @param exactRequest to match
   * @return self
   */
  public final RequestMatcher exactly(Request<?, ?> exactRequest) {
    descriptions.add("with exact request");
    assertions.add(exactRequest::equals);
    return this;
  }

  /**
   * String description of request matcher.
   *
   * @return matcher description
   */
  public final String describe() {
    return String.join("; ", descriptions);
  }

  /**
   * Fluent interface for request matcher.
   */
  public static class Fluent {

    /**
     * New instance of request matcher.
     *
     * @return RequestMatcher
     */
    public static RequestMatcher instance() {
      return new RequestMatcher();
    }

    /**
     * Create and return new matcher that matches exact given request.
     *
     * @param exactRequest to match
     * @return new matcher
     */
    public static RequestMatcher exactly(Request<?, ?> exactRequest) {
      return instance().exactly(exactRequest);
    }

    /**
     * Create and return new matcher that matches given matcher function.
     *
     * @param check matcher function to match
     * @return new matcher
     */
    public static RequestMatcher with(Function<Request<?, ?>, Boolean> check) {
      return instance().with(check);
    }

    /**
     * Create and return new matcher that matches given method.
     *
     * @param method to match
     * @return new matcher
     */
    public static RequestMatcher withMethod(String method) {
      return instance().withMethod(method);
    }

    /**
     * Create and return new matcher that matches given path.
     *
     * @param path to match
     * @return new matcher
     */
    public static RequestMatcher withPath(String path) {
      return instance().withPath(path);
    }

    /**
     * Create and return new matcher that matches given base url.
     *
     * @param baseUrl to match
     * @return new matcher
     */
    public static RequestMatcher withBaseUrl(String baseUrl) {
      return instance().withBaseUrl(baseUrl);
    }
  }

}
