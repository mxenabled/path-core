package com.mx.path.gateway.net.executors;

import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.net.Response;
import com.mx.path.model.context.tracing.CustomTracer;

import io.opentracing.Scope;
import io.opentracing.Span;

/**
 * Sets up and finalizes tracing.
 * <p>
 * Starts new span, sets propagation headers, runs next, closes span.
 * </p>
 * <p>
 * @deprecated moved to {@link com.mx.path.gateway.connect.filters.TracingFilter}
 * </p>
 */
@Deprecated
public class TracingExecutor extends RequestExecutorBase {

  public TracingExecutor(RequestExecutor next) {
    super(next);
  }

  @Override
  public final void execute(Request request, Response response) {
    Span span = CustomTracer.startSpan(request.getMethod() + ":" + request.getPath());

    try (Scope scope = CustomTracer.activateSpan(span)) {
      try {
        CustomTracer.injectSpanContextIntoHeaders(request.getHeaders());
        request.withSpanContext(span.context());
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
