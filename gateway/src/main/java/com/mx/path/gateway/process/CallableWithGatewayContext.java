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
 *
 * @param <T> T
 */
@Data
public abstract class CallableWithGatewayContext<T> implements Callable<T> {

  /**
   * -- GETTER --
   * Return request context.
   *
   * @return request context
   * -- SETTER --
   * Set request context.
   *
   * @param requestContext request context to set
   */
  private final RequestContext requestContext;

  /**
   * -- GETTER --
   * Return session.
   *
   * @return session
   * -- SETTER --
   * Set session.
   *
   * @param session session to set
   */
  private final Session session;

  /**
   * -- GETTER --
   * Return span.
   *
   * @return span
   * -- SETTER --
   * Set span.
   *
   * @param span span to set
   */
  private final Span span;

  /**
   * Default constructor.
   */
  public CallableWithGatewayContext() {
    this.session = Session.current();
    this.requestContext = RequestContext.current();
    this.span = GlobalTracer.get().activeSpan();
  }

  /**
   * Executes a callable task within a traced and session-bound context.
   *
   * @return task execution return
   * @throws Exception on failure
   */
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

  /**
   * Execute task.
   *
   * @return task return
   * @throws Exception on failure
   */
  protected abstract T execute() throws Exception;

}
