package com.mx.path.core.context;

import java.util.function.BiConsumer;

import lombok.Builder;
import lombok.Data;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.Response;

@Data
@Builder
public class UpstreamRequestProcessor {
  private BiConsumer<Request, Response> before;
  private BiConsumer<Request, Response> after;

  /**
   * Executes before block if exists
   * @param request
   * @param response
   */
  public void executeBefore(Request request, Response response) {
    if (before != null) {
      before.accept(request, response);
    }
  }

  /**
   * Executes after block if exists
   * @param request
   * @param response
   */
  public void executeAfter(Request request, Response response) {
    if (after != null) {
      after.accept(request, response);
    }
  }
}
