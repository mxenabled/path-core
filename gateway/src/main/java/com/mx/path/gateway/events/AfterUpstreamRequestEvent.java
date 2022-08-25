package com.mx.path.gateway.events;

import lombok.Builder;
import lombok.Getter;

import com.mx.common.connect.Response;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.Session;

/**
 * Fires after connection makes a request
 */
@Builder
@Getter
public class AfterUpstreamRequestEvent {
  private Response<?, ?> response;
  private final RequestContext requestContext;
  private final Session session;
}
