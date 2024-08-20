package com.mx.path.gateway.process;

import java.util.concurrent.Callable;

import lombok.Data;

import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;

/**
 * Runnable that propagates gateway context to the executing thread.
 *
 * <p>Can be used with {@link java.util.concurrent.ExecutorService} to execute concurrent code in a gateway request.
 *
 * <p>Propagates: Session.current(), RequestContext.current(), and Current Span
 * @param <T>
 */
@Data
public abstract class CallableWithGatewayContext<T> implements Callable<T> {

  private final RequestContext requestContext;
  private final Session session;
  private final Span span;

  public CallableWithGatewayContext() {
    this.session = Session.current();
    this.requestContext = RequestContext.current();
    this.span = GlobalTracer.get().activeSpan();
  }

  @Override
  public final T call() throws Exception {
    T result;

    try (Scope scope = GlobalTracer.get().activateSpan(span)) {
      Session.setCurrent(session);
      requestContext.register();

      try {
        result = execute();
      } finally {
        // Reset thread state (assume thread will be reused)
        Session.clearSession();
        RequestContext.clear();
      }
    }

    return result;
  }

  protected abstract T execute() throws Exception;

}
