package com.mx.path.core.context;

import java.util.function.BiConsumer;

import lombok.Builder;
import lombok.Data;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.Response;

/**
 * Request processor.
 */
@Data
@Builder
public class UpstreamRequestProcessor {

  /**
   * Consumer after request.
   *
   * -- GETTER --
   * Return consumer after request.
   *
   * @return consumer after request
   * -- SETTER --
   * Set consumer after request.
   *
   * @param after consumer to set
   */
  private BiConsumer<Request<?, ?>, Response<?, ?>> after;

  /**
   * Consumer before request.
   *
   * -- GETTER --
   * Return consumer before request.
   *
   * @return consumer before request
   * -- SETTER --
   * Set consumer before request.
   *
   * @param before consumer to set
   */
  private BiConsumer<Request<?, ?>, Response<?, ?>> before;

  /**
   * Execute consumer.
   *
   * @param request request
   * @param response response
   */
  public final void executeAfter(Request<?, ?> request, Response<?, ?> response) {
    if (after != null) {
      after.accept(request, response);
    }
  }

  /**
   * Execute consumer.
   *
   * @param request request
   * @param response response
   */
  public final void executeBefore(Request<?, ?> request, Response<?, ?> response) {
    if (before != null) {
      before.accept(request, response);
    }
  }
}
