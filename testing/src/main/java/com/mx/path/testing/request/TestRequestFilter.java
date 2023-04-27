package com.mx.path.testing.request;

import java.util.ArrayList;
import java.util.List;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilterBase;
import com.mx.path.core.common.connect.Response;
import com.mx.path.core.common.lang.Strings;

import org.spockframework.runtime.SpockAssertionError;

public class TestRequestFilter extends RequestFilterBase {

  public final String describe() {
    StringBuilder sb = new StringBuilder();

    if (!RequestExpectations.getRequestExpectations().isEmpty()) {
      RequestExpectations.getRequestExpectations().forEach((requestExpectation) -> sb.append(requestExpectation.getClass().getSimpleName() + ":  " + requestExpectation.getMatcher().describe() + "\n"));
      sb.append("\n\n");
    }

    return sb.toString();
  }

  @Override
  public final void execute(Request request, Response response) {
    List<RequestExpectation> handlers = new ArrayList<>();
    RequestExpectations.getRequestExpectations().forEach((expectation) -> {
      if (expectation.getMatcher().isMatch(request)) {
        handlers.add(expectation);
      }
    });

    RequestExpectations.getRequestAllowances().forEach((expectation) -> {
      if (expectation.getMatcher().isMatch(request)) {
        handlers.add(expectation);
      }
    });

    if (handlers.isEmpty()) {
      System.out.println(describe());
      throw new SpockAssertionError("No handler registered for " + describeRequest(request));
    } else {
      if (handlers.size() > 1) {
        throw new SpockAssertionError("Too many handlers registered for " + describeRequest(request));
      }
      handlers.get(0).handle(request, response);
    }

    // There won't normally be any filters after this one. Calling next for completeness
    next(request, response);
  }

  private static String describeRequest(Request<?, ?> request) {
    List<String> parts = new ArrayList<>();
    if (Strings.isNotBlank(request.getMethod())) {
      parts.add("method = " + request.getMethod());
    }

    if (Strings.isNotBlank(request.getBaseUrl())) {
      parts.add("baseUrl = " + request.getBaseUrl());
    }

    if (Strings.isNotBlank(request.getPath())) {
      parts.add("path = " + request.getPath());
    }

    return "Request: " + String.join("; ", parts);
  }
}
