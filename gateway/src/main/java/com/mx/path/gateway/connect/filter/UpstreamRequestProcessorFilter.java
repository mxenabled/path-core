package com.mx.path.gateway.connect.filter;

import java.util.List;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilterBase;
import com.mx.path.core.common.connect.Response;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.UpstreamRequestProcessor;

public class UpstreamRequestProcessorFilter extends RequestFilterBase {
  @Override
  public final void execute(Request request, Response response) {
    List<UpstreamRequestProcessor> processors = null;

    if (RequestContext.current() != null && RequestContext.current().getUpstreamRequestConfiguration() != null) {
      processors = RequestContext.current().getUpstreamRequestConfiguration().getUpstreamRequestProcessors();
    }

    if (processors != null && !processors.isEmpty()) {
      processors.forEach(processor -> {
        processor.executeBefore(request, response);
      });

      next(request, response);

      processors.forEach(processor -> {
        processor.executeAfter(request, response);
      });
    } else {
      next(request, response);
    }
  }
}
