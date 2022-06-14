package com.mx.path.gateway.net.executors;

import lombok.Getter;

import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.net.Response;

/**
 * Base class for all executors
 * <p>
 * Provides handle to next item in chain.
 * NOTE: RequestExecutors are expected to be stateless
 * </p>
 */
public abstract class RequestExecutorBase implements RequestExecutor {
  @Getter
  private RequestExecutor next;

  /**
   * @param next executor in chain
   */
  public RequestExecutorBase(RequestExecutor next) {
    this.next = next;
  }

  /**
   * Execute next executor in chain
   *
   * @param request  Request
   * @param response Response
   */
  protected final void next(Request request, Response response) {
    if (getNext() != null) {
      getNext().execute(request, response);
    }
  }

  /**
   * Does the work of the executor
   *
   * @param request  Request
   * @param response Response
   */
  @Override
  public abstract void execute(Request request, Response response);
}
