package com.mx.path.gateway.net.executors;

import com.mx.common.exception.ConnectException;
import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.net.Response;
import com.mx.path.gateway.util.UpstreamLogger;

/**
 * Handles request exceptions
 * <p>
 * Runs next
 * If Connection Failure, log call and rethrow
 * All other exceptions, placed in response
 * </p>
 * @deprecated Use {@link com.mx.path.gateway.connect.filters.ErrorHandlerFilter}
 */
@Deprecated
public class ErrorHandlerExecutor extends RequestExecutorBase {

  // Statics

  private static UpstreamLogger upstreamLogger = new UpstreamLogger();

  public static void resetUpstreamLogger() {
    ErrorHandlerExecutor.upstreamLogger = new UpstreamLogger();
  }

  public static void setUpstreamLogger(UpstreamLogger upstreamLogger) {
    ErrorHandlerExecutor.upstreamLogger = upstreamLogger;
  }

  // Constructor

  public ErrorHandlerExecutor(RequestExecutor next) {
    super(next);
  }

  // Public

  @Override
  public final void execute(Request request, Response response) {
    try {
      next(request, response);
    } catch (ConnectException e) {
      response.withException(e);

      upstreamLogger.logRequest(response);

      // Bubble up connection failures ONLY
      throw e;
    } catch (Exception e) {
      response.withException(e);
    }
  }
}
