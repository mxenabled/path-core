package com.mx.path.gateway.connect.filter;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilterBase;
import com.mx.path.core.common.connect.Response;
import com.mx.path.core.context.tracing.HttpHeadersCarrier;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

/**
 * Sets up and finalizes tracing.
 *
 * <p>Starts new span, sets propagation headers, runs next, closes span.
 */
public class TracingFilter extends RequestFilterBase {
  @Override
  public final void execute(Request request, Response response) {
    Tracer tracer = GlobalTracer.get();

    if (tracer == null) {
      next(request, response);
      return;
    }

    Span span = tracer.buildSpan(request.getTraceKey())
        .start();
    // Note: The scope in the try with resource block below
    // will be automatically closed at the end of the code block.
    // If you do not use a try with resource statement, you need
    // to call scope.close().
    try (Scope scope = tracer.activateSpan(span)) {

      tracer.inject(tracer.activeSpan().setTag(Tags.SPAN_KIND, Tags.SPAN_KIND_CLIENT).context(), Format.Builtin.HTTP_HEADERS, new HttpHeadersCarrier(request.getHeaders()));

      request.setTraceId(span.context().toTraceId());
      request.setTraceSpanId(span.context().toSpanId());
      next(request, response);

    } catch (Exception e) {
      span.log(e.getMessage());
      throw e;
    } finally {
      span.finish();
    }
  }
}
