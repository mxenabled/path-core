package com.mx.path.gateway.connect.filter;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilterBase;
import com.mx.path.core.common.connect.Response;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.UpstreamRequestConfiguration;

public class UpstreamRequestProcessorFilter extends RequestFilterBase {
  @Override
  public final void execute(Request request, Response response) {
    UpstreamRequestConfiguration upstreamRequestConfiguration = null;

    if (RequestContext.current() != null) {
      upstreamRequestConfiguration = RequestContext.current().getUpstreamRequestConfiguration();
    }

    if (upstreamRequestConfiguration != null && upstreamRequestConfiguration.getUpstreamRequestProcessors() != null) {
      upstreamRequestConfiguration.getUpstreamRequestProcessors().forEach(processor -> processor.executeBefore(request, response));
    }

    next(request, response);

    if (upstreamRequestConfiguration != null && upstreamRequestConfiguration.getUpstreamRequestProcessors() != null) {
      upstreamRequestConfiguration.getUpstreamRequestProcessors().forEach(processor -> processor.executeAfter(request, response));
    }
  }
}
