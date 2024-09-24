package com.mx.path.testing.request;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.Response;

import org.spockframework.runtime.SpockAssertionError;

/**
 * Test utility class for requests with individual request parameters.
 */
public class RequestExpectation {

  /**
   * Test helper class for defining and managing expectations for requests.
   *
   * @param matcher used to match requests with defined criteria
   */
  public RequestExpectation(RequestMatcher matcher) {
    this.matcher = matcher;
  }

  /**
   * Return request matcher.
   *
   * @return matcher
   */
  @Getter
  private RequestMatcher matcher;

  /**
   * Return request handler.
   *
   * @return handler
   */
  @Getter
  private BiConsumer<Request<?, ?>, Response<?, ?>> handler;

  private BiConsumer<Request<?, ?>, Response<?, ?>> onRequest;

  /**
   * -- SETTER --
   * Set number of times request was invoked.
   *
   * @param invokeCount value to set
   * -- GETTER --
   * Return number of times request was invoked.
   *
   * @return invoke count
   */
  @Setter
  @Getter
  private int invokeCount = 0;

  /**
   * -- SETTER --
   * Set how mane times you expect this request to be invoked.
   *
   * @param expectedInvokeCount value to set
   * -- GETTER --
   * Get how mane times you expect this request to be invoked.
   *
   * @return expected invoke count
   */
  @Setter
  @Getter
  private int expectedInvokeCount = 1;

  private final List<Request<?, ?>> receivedRequests = new ArrayList<>();

  /**
   * Get the first request received by this expectation.
   *
   * @return list of requests in order they were received
   */
  public final Request<?, ?> getReceivedRequest() {
    assertInvoked();
    return receivedRequests.get(0);
  }

  /**
   * Filter received requests based on provided matcher and returns first request from filtered list or null if none is found.
   *
   * @param requestMatcher to use in filtering process
   * @return first request from filtered list or null if none is found
   */
  public final Request<?, ?> getReceivedRequest(RequestMatcher requestMatcher) {
    assertInvoked();
    return receivedRequests.stream().filter(requestMatcher::isMatch).findFirst().orElse(null);
  }

  /**
   * Get the requests received by this expectation.
   *
   * @return list of requests in order they were received
   */
  public final List<Request<?, ?>> getReceivedRequests() {
    assertInvoked();
    return receivedRequests;
  }

  /**
   * Get the requests received by this expectation that match the provided request matcher.
   *
   * @param requestMatcher to use in filtering process
   * @return filtered list of requests in order they were received
   */
  public final List<Request<?, ?>> getReceivedRequests(RequestMatcher requestMatcher) {
    assertInvoked();
    return receivedRequests.stream().filter(requestMatcher::isMatch).collect(Collectors.toList());
  }

  /**
   * Handle provided request and response.
   *
   * @param request  request to handle
   * @param response response to handle
   */
  public final void handle(Request<?, ?> request, Response<?, ?> response) {
    receivedRequests.add(request);
    if (onRequest != null) {
      onRequest.accept(request, response);
    }
    handler.accept(request, response);
    invokeCount++;
  }

  /**
   * Set how many times you expect request to be invoked.
   *
   * @param times expected invoke times
   * @return this request expectation
   */
  public final RequestExpectation times(int times) {
    expectedInvokeCount = times;
    return this;
  }

  /**
   * Set onRequest of type BiConsumer to be executed on each request handled.
   *
   * @param consumer to se onRequest
   * @return this request expectation
   */
  public final RequestExpectation onRequest(BiConsumer<Request<?, ?>, Response<?, ?>> consumer) {
    this.onRequest = consumer;
    return this;
  }

  /**
   * Set handler of type BiConsumer to be executed on each request handled.
   *
   * @param responder to set on handler
   * @return this request expectation
   */
  public final RequestExpectation toRespond(BiConsumer<Request<?, ?>, Response<?, ?>> responder) {
    this.handler = responder;
    return this;
  }

  /**
   * Compare number of requests invoked with expectation, if expectation not met throws exception.
   */
  public final void assertInvoked() {
    if (expectedInvokeCount != invokeCount) {
      throw new SpockAssertionError("Expectation (" + matcher.describe() + ") not met. Expected " + expectedInvokeCount + " invocations, but received " + invokeCount);
    }
  }

  /**
   * Return new request expectation with onRequest consumer set to add each new request o requests list.
   *
   * @return RequestExpectation
   */
  public final RequestExpectation collectRequests() {
    return onRequest((request, response) -> RequestExpectations.addRequest(request));
  }
}
