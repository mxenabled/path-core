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

public class RequestExpectation {
  public RequestExpectation(RequestMatcher matcher) {
    this.matcher = matcher;
  }

  @Getter
  private RequestMatcher matcher;

  @Getter
  private BiConsumer<Request<?, ?>, Response<?, ?>> handler;

  private BiConsumer<Request<?, ?>, Response<?, ?>> onRequest;

  @Setter
  @Getter
  private int invokeCount = 0;

  @Setter
  @Getter
  private int expectedInvokeCount = 1;

  private final List<Request<?, ?>> receivedRequests = new ArrayList<>();

  /**
   * Get the first request received by this expectation
   * @return list of requests in order they were received
   */
  public final Request<?, ?> getReceivedRequest() {
    assertInvoked();
    return receivedRequests.get(0);
  }

  public final Request<?, ?> getReceivedRequest(RequestMatcher requestMatcher) {
    assertInvoked();
    return receivedRequests.stream().filter(requestMatcher::isMatch).findFirst().orElse(null);
  }

  /**
   * Get the requests received by this expectation
   * @return list of requests in order they were received
   */
  public final List<Request<?, ?>> getReceivedRequests() {
    assertInvoked();
    return receivedRequests;
  }

  /**
   * Get the requests received by this expectation that match the provided request matcher
   * @param requestMatcher
   * @return filtered list of requests in order they were received
   */
  public final List<Request<?, ?>> getReceivedRequests(RequestMatcher requestMatcher) {
    assertInvoked();
    return receivedRequests.stream().filter(requestMatcher::isMatch).collect(Collectors.toList());
  }

  public final void handle(Request<?, ?> request, Response<?, ?> response) {
    receivedRequests.add(request);
    if (onRequest != null) {
      onRequest.accept(request, response);
    }
    handler.accept(request, response);
    invokeCount++;
  }

  public final RequestExpectation times(int times) {
    expectedInvokeCount = times;
    return this;
  }

  public final RequestExpectation onRequest(BiConsumer<Request<?, ?>, Response<?, ?>> consumer) {
    this.onRequest = consumer;
    return this;
  }

  public final RequestExpectation toRespond(BiConsumer<Request<?, ?>, Response<?, ?>> responder) {
    this.handler = responder;
    return this;
  }

  public final void assertInvoked() {
    if (expectedInvokeCount != invokeCount) {
      throw new SpockAssertionError("Expectation (" + matcher.describe() + ") not met. Expected " + expectedInvokeCount + " invocations, but received " + invokeCount);
    }
  }

  public final RequestExpectation collectRequests() {
    return onRequest((request, response) -> RequestExpectations.addRequest(request));
  }
}
