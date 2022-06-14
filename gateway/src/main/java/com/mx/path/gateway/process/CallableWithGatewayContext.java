package com.mx.path.gateway.process;

import java.util.concurrent.Callable;

import lombok.Data;

import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.Session;
import com.mx.path.model.context.tracing.CustomTracer;

import io.opentracing.Scope;
import io.opentracing.Span;

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
    this.span = CustomTracer.getTracer().activeSpan();
  }

  @Override
  public final T call() throws Exception {
    T result;

    try (Scope scope = CustomTracer.getTracer().activateSpan(span)) {
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
