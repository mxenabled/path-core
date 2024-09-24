package com.mx.path.core.context.tracing;

import java.util.Map;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;

/**
 * Utility class for managing tracers.
 */
public class CustomTracer {
  private static Tracer tracer;

  // Public

  /**
   * Set tracer.
   *
   * @param tracer tracer to set
   */
  public static void setTracer(Tracer tracer) {
    CustomTracer.tracer = tracer;
  }

  /**
   * Get tracer.
   *
   * @return tracer
   */
  public static Tracer getTracer() {
    return tracer;
  }

  /**
   * Get span id.
   *
   * @return span id
   */
  public static String getSpanId() {
    if (getSpanContext() != null) {
      return getSpanContext().toSpanId();
    }

    return null;
  }

  /**
   * Get trace id.
   *
   * @return trace id
   */
  public static String getTraceId() {
    if (getSpanContext() != null) {
      return getSpanContext().toTraceId();
    }

    return null;
  }

  /**
   * Get tracer span context.
   *
   * @return tracer span context
   */
  public static SpanContext getSpanContext() {
    if (tracer != null && tracer.activeSpan() != null) {
      return tracer.activeSpan().context();
    }

    return null;
  }

  /**
   * Start a child span with given name.
   *
   * @param name of process
   * @return child span
   */
  public static Span startSpan(String name) {
    if (tracer != null) {
      return tracer
          .buildSpan(name)
          .start();
    }
    return null;
  }

  /**
   * Starts a child span with name and sets the parent as spanContext.
   *
   * @param name span name
   * @param spanContext span context
   * @return span
   */
  public static Span startChildSpanFromSpanContext(String name, SpanContext spanContext) {
    Span span = tracer
        .buildSpan(name)
        .asChildOf(spanContext)
        .start();
    return span;
  }

  /**
   * Injects the {@link SpanContext} into the supplied headers.
   *
   * @param headers headers
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
   * Activates the provided span and returns a Scope object that must be explicitly closed (or implicitly via `try (Scope scope = ...)`).
   *
   * @param span span
   * @return scope
   */
  public static Scope activateSpan(Span span) {
    return tracer.activateSpan(span);
  }
}
