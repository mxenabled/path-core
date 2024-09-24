package com.mx.path.testing.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilter;
import com.mx.path.gateway.accessor.AccessorConnectionBase;
import com.mx.path.gateway.connect.filter.CallbacksFilter;
import com.mx.path.gateway.connect.filter.ErrorHandlerFilter;
import com.mx.path.gateway.connect.filter.RequestFinishedFilter;

import org.mockito.Mockito;

/**
 * Test utility class for requests.
 */
public class RequestExpectations {

  /**
   * Allowed requests (not verified).
   */
  private static final ThreadLocal<List<RequestExpectation>> REQUEST_ALLOWANCES = ThreadLocal.withInitial(ArrayList::new);

  /**
   * Expected requests.
   */
  private static final ThreadLocal<List<RequestExpectation>> REQUEST_EXPECTATIONS = ThreadLocal.withInitial(ArrayList::new);

  /**
   * Ordered list of requests received.
   */
  private static final ThreadLocal<List<Request<?, ?>>> REQUESTS = ThreadLocal.withInitial(ArrayList::new);

  /**
   * Return list of allowed request expectations.
   *
   * @return list of allowed request expectations
   */
  public static List<RequestExpectation> getRequestAllowances() {
    return REQUEST_ALLOWANCES.get();
  }

  /**
   * Return list of expected request expectations.
   *
   * @return list of expected request expectations
   */
  public static List<RequestExpectation> getRequestExpectations() {
    return REQUEST_EXPECTATIONS.get();
  }

  /**
   * Return ordered list of requests received.
   *
   * @return requests received
   */
  private static List<Request<?, ?>> getRequests() {
    return REQUESTS.get();
  }

  /**
   * Capture and store a handled request.
   *
   * This is called every time a request is matched and handled by any expectation
   * @param request received request
   */
  public static void addRequest(Request<?, ?> request) {
    getRequests().add(request);
  }

  /**
   * Alias to {@link #stubConnection(RequestMatcher)}.
   *
   * @param requestMatcher to use on requests handled
   * @return new request expectation
   */
  public static RequestExpectation allowConnection(RequestMatcher requestMatcher) {
    return stubConnection(requestMatcher);
  }

  /**
   * Create a request expectation. Will fail verification if not invoked.
   *
   * @param requestMatcher matching rules
   * @return self
   */
  public static RequestExpectation expectConnection(RequestMatcher requestMatcher) {
    return Fluent.expectRequest(requestMatcher);
  }

  /**
   * Get the first request executed against expectations.
   *
   * @return first request
   */
  public static Request<?, ?> request() {
    return requests().stream().findFirst().orElse(null);
  }

  /**
   * Get the first matching request executed against expectations.
   *
   * @param requestMatcher matching rules
   * @return first request
   */
  public static Request<?, ?> request(RequestMatcher requestMatcher) {
    return requests(requestMatcher).stream().findFirst().orElse(null);
  }

  /**
   * Get the requests received by expectations (does not include allowances). Requests are in the order they were received.
   *
   * @return list of requests
   */
  public static List<Request<?, ?>> requests() {
    return REQUESTS.get();
  }

  /**
   * Get the filtered requests received by expectations (does not include allowances). Requests are in the order they were received.
   *
   * @param requestMatcher to apply on filter
   * @return list of requests
   */
  public static List<Request<?, ?>> requests(RequestMatcher requestMatcher) {
    return requests().stream().filter(requestMatcher::isMatch).collect(Collectors.toList());
  }

  /**
   * Clears out expectations. Should be invoked between tests.
   */
  public static void reset() {
    REQUEST_EXPECTATIONS.remove();
    REQUEST_ALLOWANCES.remove();
    REQUESTS.remove();
  }

  /**
   * Creates a request expectation that is not expected to be invoked.
   *
   * @param requestMatcher to apply on filter
   * @return new expectation
   */
  public static RequestExpectation stubConnection(RequestMatcher requestMatcher) {
    return Fluent.stubRequest(requestMatcher);
  }

  /**
   * Verifies that all expected requests were made. Raises exception if expectations are not met.
   *
   * @return true if all requests were made. (this return value allows this to be invoked in {@code then:} section of Spock test.)
   */
  public static boolean verifyConnectionExpectations() {
    return Fluent.verifyRequestExpectations();
  }

  /**
   * Helper object to allow fluent interface.
   */
  public static class Fluent {

    /**
     * Return list of default request filters.
     *
     * @return list of default request filters
     */
    public static List<RequestFilter> connectionBaseRequestFilters() {
      return Arrays.asList(
          new ErrorHandlerFilter(),
          new CallbacksFilter(),
          new RequestFinishedFilter());
    }

    /**
     * Create a request expectation.Will fail verification if not invoked.
     *
     * @param requestMatcher for request expectation
     * @return new request expectation
     */
    public static RequestExpectation expectRequest(RequestMatcher requestMatcher) {
      RequestExpectation requestExpectation = new RequestExpectation(requestMatcher)
          .onRequest((request, response) -> {
            addRequest(request);
          });
      getRequestExpectations().add(requestExpectation);
      return requestExpectation;
    }

    /**
     * Setup basic parameters for mock connection.
     *
     * @param connection to set up and mock
     * @return new mock connection
     */
    public static AccessorConnectionBase<?> setupConnection(AccessorConnectionBase<?> connection) {
      AccessorConnectionBase<?> connectionSpy = Mockito.spy(connection);
      Mockito.doReturn(connectionBaseRequestFilters()).when(connectionSpy).getBaseRequestFilters();
      Mockito.doReturn(Arrays.asList(new TestRequestFilter())).when(connectionSpy).connectionRequestFilters();

      return connectionSpy;
    }

    /**
     * Create and stub a new request expectation based on the given request matcher.
     *
     * @param request request matcher that defines the criteria for matching a request
     * @return new request expectation object that represents the stubbed request expectation
     */
    public static RequestExpectation stubRequest(RequestMatcher request) {
      RequestExpectation requestExpectation = new RequestExpectation(request);
      getRequestAllowances().add(requestExpectation);
      return requestExpectation;
    }

    /**
     * Check if each request expectation was fulfilled, throw exception if not.
     *
     * @return true if all fulfilled
     */
    public static boolean verifyRequestExpectations() {
      getRequestExpectations().forEach(RequestExpectation::assertInvoked);
      return true;
    }
  }
}
