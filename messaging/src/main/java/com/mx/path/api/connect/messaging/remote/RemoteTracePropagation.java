package com.mx.path.api.connect.messaging.remote;

import com.mx.path.api.connect.messaging.Message;
import com.mx.path.model.context.tracing.CustomTracer;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;

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
    SpanContext extracted = CustomTracer.getTracer().extract(Format.Builtin.TEXT_MAP, new MessageHeadersExtractAdaptor(message.getMessageHeaders()));
    return CustomTracer.startChildSpanFromSpanContext(message.getChannel(), extracted);
  }

  /**
   * Inject tracing propagation headers into event
   * @param message
   */
  public static void inject(Message message) {
    SpanContext spanContext = CustomTracer.getSpanContext();
    if (spanContext != null) {
      // Setting SPAN_KIND to SPAN_KIND_CLIENT sets the correct formatter for trace-header propagation.
      CustomTracer.getTracer().inject(CustomTracer.getTracer().activeSpan().setTag(Tags.SPAN_KIND, Tags.SPAN_KIND_CLIENT).context(), Format.Builtin.TEXT_MAP, new MessageHeadersCarrier(message.getMessageHeaders()));
    }
  }

}
