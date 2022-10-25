package com.mx.path.gateway.connect.filters;

import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilterBase;
import com.mx.common.connect.Response;

/**
 * Request finisher
 *
 * <p>Ensures that the request is set to finished so all timing is set before exceptions go further up the stack.
 */
public class RequestFinishedFilter extends RequestFilterBase {
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
