package com.mx.path.connect.messaging.remote;

import com.mx.path.connect.messaging.Message;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

/**
 * Injects and extracts Message trace propagation headers using B3 format.
 */
public class RemoteTracePropagation {
  /**
   * Extract propagation headers and set next span in context
   * @param message
   * @return new Span
   */
  public static Span extract(Message message) {
    Tracer tracer = GlobalTracer.get();
    SpanContext extracted = tracer.extract(Format.Builtin.TEXT_MAP, new MessageHeadersExtractAdaptor(message.getMessageHeaders()));
    return GlobalTracer.get().buildSpan(message.getChannel()).asChildOf(extracted).start();
  }

  /**
   * Inject tracing propagation headers into event
   * @param message
   */
  public static void inject(Message message) {
    Tracer tracer = GlobalTracer.get();
    Span span = tracer.activeSpan();
    SpanContext spanContext = span.context();
    if (spanContext != null) {
      // Setting SPAN_KIND to SPAN_KIND_CLIENT sets the correct formatter for trace-header propagation.
      GlobalTracer.get().inject(GlobalTracer.get().activeSpan().setTag(Tags.SPAN_KIND, Tags.SPAN_KIND_CLIENT).context(), Format.Builtin.TEXT_MAP, new MessageHeadersCarrier(message.getMessageHeaders()));
    }
  }

}
