package com.mx.path.gateway.connect.filters;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mx.common.collections.MultiValueMap;
import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilterBase;
import com.mx.common.connect.Response;
import com.mx.path.model.context.tracing.CustomTracer;

import io.opentracing.Scope;
import io.opentracing.Span;

/**
 * Sets up and finalizes tracing.
 * <p>
 * Starts new span, sets propagation headers, runs next, closes span.
 * </p>
 */
public class TracingFilter extends RequestFilterBase {
  @Override
  public final void execute(Request request, Response response) {
    Span span = CustomTracer.startSpan(request.getTraceKey());
    if (span == null) {
      next(request, response);
      return;
    }

    try (Scope scope = CustomTracer.activateSpan(span)) {
      try {
        Map<String, String> traceHeaders = new LinkedHashMap<>();

        CustomTracer.injectSpanContextIntoHeaders(traceHeaders);
        MultiValueMap<String, String> requestHeaders = new MultiValueMap<>(request.getHeaders());
        traceHeaders.forEach(requestHeaders::add);

        request.setTraceId(span.context().toTraceId());
        request.setTraceSpanId(span.context().toSpanId());
        next(request, response);
      } catch (Exception e) {
        span.log(e.getMessage());
        throw e;
      }
    } finally {
      span.finish();
    }
  }
}
