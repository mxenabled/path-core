package testing.connect;

import java.util.function.BiConsumer;

import lombok.Getter;
import lombok.Setter;

import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilterBase;
import com.mx.common.connect.Response;

public class RequestFilterStub extends RequestFilterBase {
  @Getter
  @Setter
  private BiConsumer<Request, Response> spy;

  @Override
  public void execute(Request request, Response response) {
    if (getSpy() != null) {
      getSpy().accept(request, response);
    }
  }

  public final RequestFilterStub withSpy(BiConsumer<Request, Response> spy) {
    setSpy(spy);
    return this;
  }
}
