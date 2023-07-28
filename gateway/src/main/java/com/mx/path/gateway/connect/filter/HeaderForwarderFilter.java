package com.mx.path.gateway.connect.filter;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilterBase;
import com.mx.path.core.common.connect.Response;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.UpstreamRequestConfiguration;

/**
 * Request Filter that adds forwarded headers to request
 */
public class HeaderForwarderFilter extends RequestFilterBase {

  @Override
  public final void execute(Request request, Response response) {
    UpstreamRequestConfiguration context = null;
    if (RequestContext.current() != null) {
      context = RequestContext.current().getUpstreamRequestConfiguration();
    }

    if (context != null && context.getForwardedRequestHeaders() != null) {
      context.getForwardedRequestHeaders().forEach((key, value) -> {
        request.withHeader(key, value.toString());
      });
    }

    next(request, response);
  }
}
