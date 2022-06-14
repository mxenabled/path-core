package com.mx.path.model.context.tracing;

import java.util.Map;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;

public class CustomTracer {
  private static Tracer tracer;

  // Public

  public static void setTracer(Tracer tracer) {
    CustomTracer.tracer = tracer;
  }

  public static Tracer getTracer() {
    return tracer;
  }

  public static String getSpanId() {
    if (getSpanContext() != null) {
      return getSpanContext().toSpanId();
    }

    return null;
  }

  public static String getTraceId() {
    if (getSpanContext() != null) {
      return getSpanContext().toTraceId();
    }

    return null;
  }

  public static SpanContext getSpanContext() {
    if (tracer != null && tracer.activeSpan() != null) {
      return tracer.activeSpan().context();
    }

    return null;
  }

  /**
   * Start a child span with name
   * @param name of process
   * @return child span
   */
  public static Span startSpan(String name) {
    return tracer
        .buildSpan(name)
        .start();
  }

  /**
   * Starts a child span with name and sets the parent as spanContext
   * @param name
   * @param spanContext
   * @return
   */
  public static Span startChildSpanFromSpanContext(String name, SpanContext spanContext) {
    Span span = tracer
        .buildSpan(name)
        .asChildOf(spanContext)
        .start();
    return span;
  }

  /**
   * Injects the SpanContext into the supplied headers.
   * @param headers
   */
  public static void injectSpanContextIntoHeaders(Map<String, String> headers) {
    if (getSpanContext() != null) {
      // We have to set the SPAN_KIND here because the Tracing implementation that Web uses (Brave) manages many instances of SpanContexts. Attempting to set the SPAN_KIND
      // anywhere else but here will not modify the correct SpanContext and will cause the injector to inject the headers in the incorrect format. This should not affect other
      // Tracing instrumentation, because this is a valid spot to set the SpanKind.
      tracer.inject(tracer.activeSpan().setTag(Tags.SPAN_KIND, Tags.SPAN_KIND_CLIENT).context(), Format.Builtin.HTTP_HEADERS, new HttpHeadersCarrier(headers));
    }
  }

  /**
   * Activates the provided span and returns a Scope object that must be explicitly closed (or implicitly via `try (Scope scope = ...)`)
   * @param span
   * @return
   */
  public static Scope activateSpan(Span span) {
    return tracer.activateSpan(span);
  }
}
