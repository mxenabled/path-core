package com.mx.path.gateway.util;

import java.util.function.BiConsumer;

import lombok.Setter;

import com.google.common.eventbus.Subscribe;
import com.mx.path.gateway.events.AfterUpstreamRequestEvent;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.Session;

/**
 * Listens for {@link AfterUpstreamRequestEvent} and sends the request to {@link UpstreamLogger}
 */
public class UpstreamRequestLoggingEventListener {
  @Setter
  private static UpstreamLogger upstreamLogger = new UpstreamLogger();

  @Subscribe
  public final void onAfterUpstreamRequestEvent(AfterUpstreamRequestEvent afterUpstreamRequestEvent) {
    withContext(afterUpstreamRequestEvent.getSession(), afterUpstreamRequestEvent.getRequestContext(), (req, sess) -> {
      if (afterUpstreamRequestEvent.getResponse() != null) {
        upstreamLogger.logRequest(afterUpstreamRequestEvent.getResponse());
      }
    });
  }

  private void withContext(Session session, RequestContext requestContext, BiConsumer<Session, RequestContext> block) {
    Session currentSession = Session.current();
    if (session != null) {
      Session.setCurrent(session);
    }

    RequestContext currentRequestContext = RequestContext.current();
    if (requestContext != null) {
      requestContext.register();
    }

    try {
      block.accept(session, requestContext);
    } finally {
      if (currentRequestContext != null) {
        currentRequestContext.register();
      } else {
        RequestContext.clear();
      }

      if (currentSession != null) {
        Session.setCurrent(currentSession);
      } else {
        Session.clearSession();
      }
    }
  }
}
