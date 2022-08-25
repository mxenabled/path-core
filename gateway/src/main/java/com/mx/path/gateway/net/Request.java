package com.mx.path.gateway.net;

import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import io.opentracing.SpanContext;

/**
 * @deprecated use {@link com.mx.common.connect.Request}
 */
@Deprecated
@SuppressFBWarnings("NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public class Request extends com.mx.common.connect.Request<Request, Response> {
  public final com.mx.common.connect.Request withSpanContext(SpanContext context) {
    if (Objects.nonNull(context)) {
      setTraceId(context.toTraceId());
      setTraceSpanId(context.toSpanId());
    }

    return this;
  }

  @Override
  public final Response execute() {
    // Get a processor that is fit for this request and process the request
    return RequestProcessorFactory.forRequest(this).process(this);
  }
}
