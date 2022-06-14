package com.mx.path.gateway.net;

import com.mx.path.gateway.net.executors.CallbacksExecutor;
import com.mx.path.gateway.net.executors.ErrorHandlerExecutor;
import com.mx.path.gateway.net.executors.FaultTolerantRequestExecutor;
import com.mx.path.gateway.net.executors.HttpClientExecutor;
import com.mx.path.gateway.net.executors.RequestExecutor;
import com.mx.path.gateway.net.executors.RequestFinishedExecutor;
import com.mx.path.gateway.net.executors.TracingExecutor;
import com.mx.path.gateway.net.executors.UpstreamRequestEventExecutor;

public class HttpClientRequestProcessor implements RequestProcessor {

  private static RequestExecutor executor;

  // Build HttpClient executor decorator
  static {
    executor = new TracingExecutor(
        new UpstreamRequestEventExecutor(
            new ErrorHandlerExecutor(
                new CallbacksExecutor(
                    new RequestFinishedExecutor(
                        new FaultTolerantRequestExecutor(
                            new HttpClientExecutor(null)))))));
  }

  public static RequestProcessor forRequest(Request request) {
    return new HttpClientRequestProcessor();
  }

  @Override
  public final Response process(Request request) {
    return executor.execute(request);
  }
}
