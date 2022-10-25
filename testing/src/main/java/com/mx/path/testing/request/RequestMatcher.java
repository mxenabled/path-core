package com.mx.path.testing.request;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.mx.common.connect.Request;

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

  public final boolean isMatch(Request<?, ?> request) {
    return assertions.stream().allMatch((c) -> c.apply(request));
  }

  public final RequestMatcher withBaseUrl(String baseUrl) {
    descriptions.add("baseUrl == " + baseUrl);
    assertions.add((request) -> baseUrl.equals(request.getBaseUrl()));
    return this;
  }

  public final RequestMatcher withMethod(String method) {
    descriptions.add("method == " + method);
    assertions.add((request) -> method.equals(request.getMethod()));
    return this;
  }

  public final RequestMatcher withPath(String path) {
    descriptions.add("path == " + path);
    assertions.add((request) -> path.equals(request.getPath()));
    return this;
  }

  public final RequestMatcher with(Function<Request<?, ?>, Boolean> check) {
    descriptions.add("with check");
    assertions.add(check);
    return this;
  }

  public final RequestMatcher exactly(Request<?, ?> exactRequest) {
    descriptions.add("with exact request");
    assertions.add(exactRequest::equals);
    return this;
  }

  public final String describe() {
    return String.join("; ", descriptions);
  }

  public static class Fluent {
    public static RequestMatcher instance() {
      return new RequestMatcher();
    }

    public static RequestMatcher exactly(Request<?, ?> exactRequest) {
      return instance().exactly(exactRequest);
    }

    public static RequestMatcher with(Function<Request<?, ?>, Boolean> check) {
      return instance().with(check);
    }

    public static RequestMatcher withMethod(String method) {
      return instance().withMethod(method);
    }

    public static RequestMatcher withPath(String path) {
      return instance().withPath(path);
    }

    public static RequestMatcher withBaseUrl(String baseUrl) {
      return instance().withBaseUrl(baseUrl);
    }
  }

}
