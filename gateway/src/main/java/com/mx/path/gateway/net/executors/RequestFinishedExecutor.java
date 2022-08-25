package com.mx.path.gateway.net.executors;

import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.net.Response;

/**
 * Request finisher
 * <p>
 * Ensures that the request is set to finished so all timing is set before exceptions go further up the stack.
 * </p>
 * <p>
 * @deprecated moved to {@link com.mx.path.gateway.connect.filters.RequestFinishedFilter}
 * </p>
 */
@Deprecated
public class RequestFinishedExecutor extends RequestExecutorBase {
  public RequestFinishedExecutor(RequestExecutor next) {
    super(next);
  }

  @Override
  public final void execute(Request request, Response response) {
    try {
      next(request, response);
    } finally {
      // This is an idempotent operation (after the initial call).
      // This just makes sure we have finished off the request before moving up the stack.
      // Only here to make sure called in case of an error before the request is executed.
      response.finish();
    }
  }
}
