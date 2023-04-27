package com.mx.path.core.common.connect;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for all executors
 *
 * <p>Provides handle to next item in chain.
 *
 * <p>NOTE: RequestExecutors are expected to be stateless
 */
public abstract class RequestFilterBase implements RequestFilter {
  @Getter
  @Setter
  private RequestFilter next;

  /**
   * @param next executor in chain
   */
  public RequestFilterBase(RequestFilter next) {
    this.next = next;
  }

  public RequestFilterBase() {
  }

  /**
   * Execute next executor in chain
   *
   * @param request  Request
   * @param response Response
   */
  @Override
  public final void next(Request request, Response response) {
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
