package com.mx.path.core.context;

import java.util.function.BiConsumer;

import lombok.Builder;
import lombok.Data;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.Response;

@Data
@Builder
public class UpstreamRequestProcessor {
  private BiConsumer<Request<?, ?>, Response<?, ?>> after;
  private BiConsumer<Request<?, ?>, Response<?, ?>> before;

  public final void executeAfter(Request<?, ?> request, Response<?, ?> response) {
    if (after != null) {
      after.accept(request, response);
    }
  }

  public final void executeBefore(Request<?, ?> request, Response<?, ?> response) {
    if (before != null) {
      before.accept(request, response);
    }
  }
}
