package com.mx.path.connect.http;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mx.path.core.common.connect.RequestFilter;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.common.request.Feature;
import com.mx.path.core.context.RequestContext;
import com.mx.path.gateway.accessor.AccessorConnectionBase;

/**
 * Represents an HTTP connection.
 */
public class HttpAccessorConnection extends AccessorConnectionBase<HttpRequest> {
  /**
   * Perform http delete.
   * @param path path to the targeted resource
   * @return {@link HttpResponse} resulting from the delete request
   */
  public HttpResponse delete(String path) {
    return request(path).delete();
  }

  /**
   * Perform http get.
   * @param path path to the targeted resource
   * @return {@link HttpResponse} resulting from the get request
   */
  public HttpResponse get(String path) {
    return request(path).get();
  }

  /**
   * Perform http patch.
   * @param path path to the targeted resource
   * @param body body of request
   * @return {@link HttpResponse} resulting from the patch request
   */
  public HttpResponse patch(String path, Object body) {
    return request(path).patch();
  }

  /**
   * Perform http post.
   * @param path path to the targeted resource
   * @param body body of request
   * @return {@link HttpResponse} resulting from the patch request
   */
  public HttpResponse post(String path, Object body) {
    return request(path).withBody(body).post();
  }

  /**
   * Perform http put.
   * @param path path to the targeted resource
   * @param body body of request
   * @return {@link HttpResponse} resulting from the patch request
   */
  public HttpResponse put(String path, Object body) {
    return request(path).withBody(body).put();
  }

  /**
   * Prepare request based on bound configurations.
   * @return request
   */
  @Override
  public HttpRequest request(String path) {
    HttpRequest request = new HttpRequest(buildFilterChain())
        .withPath(path)
        .withBaseUrl(getBaseUrl())
        .withConnectionSettings(this);

    if (RequestContext.current() != null && Strings.isNotBlank(RequestContext.current().getFeature())) {
      request.withFeature(Feature.valueOf(Feature.class, RequestContext.current().getFeature().toUpperCase()));
    }

    return request;
  }

  /**
   * @return List of filters to be executed at the end of the chain.
   */
  @Override
  public List<RequestFilter> connectionRequestFilters() {
    return Arrays.asList(new HttpClientFilter());
  }

  // Protected Methods

  /**
   * Executes an HTTP request with specified method, path, body, and query parameters.
   *
   * @param method HTTP request method (e.g., GET, POST, DELETE)
   * @param path target URL or resource path for request
   * @param body request body to be sent
   * @param queryString a map of query string parameters to be appended to the URL, can be null
   * @return the {@link HttpResponse} resulting from the executed request
   */
  protected HttpResponse execute(String method, String path, Object body, Map<String, String> queryString) {
    HttpRequest request = request(path)
        .withMethod(method)
        .withBaseUrl(getBaseUrl())
        .withBody(body);

    if (queryString != null) {
      request.getQueryStringParams().putAll(queryString);
    }

    return request.execute();
  }
}
