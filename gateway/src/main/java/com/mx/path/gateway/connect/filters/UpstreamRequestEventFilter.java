package com.mx.path.gateway.connect.filters;

import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilterBase;
import com.mx.common.connect.Response;
import com.mx.common.events.EventBus;
import com.mx.path.gateway.events.AfterUpstreamRequestEvent;
import com.mx.path.gateway.events.BeforeUpstreamRequestEvent;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.facility.Facilities;

/**
 * UpstreamRequestEventExecutor posts request and response events
 *
 * <p>This executor posts an event before and after the rest of the executor stack executes.
 * These events can be used for logging, request/response inspection, etc.
 * Uses the client-configured EventBus facility to post the events
 */
public class UpstreamRequestEventFilter extends RequestFilterBase {
  @Override
  public final void execute(Request request, Response response) {
    RequestContext requestContext = RequestContext.current();

    EventBus eventBus = null;
    if (requestContext != null && requestContext.getClientId() != null) {
      eventBus = Facilities.getEventBus(requestContext.getClientId());
    }

    if (eventBus != null) {
      eventBus.post(BeforeUpstreamRequestEvent.builder().request(request).build());
    }

    next(request, response);

    if (eventBus != null) {
      eventBus.post(AfterUpstreamRequestEvent.builder().response(response).build());
    }
  }
}
