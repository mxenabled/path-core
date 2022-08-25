package com.mx.accessors;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import com.mx.common.collections.ObjectMap;

/**
 * todo: Move back to gateway after model extraction
 * @param <T>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("checkstyle:HiddenField")
public class AccessorResponse<T> {

  private Throwable exception;
  @Singular
  private final Map<String, String> headers = new LinkedHashMap<>();
  private final ObjectMap metadata = new ObjectMap();
  private T result;
  private AccessorResponseStatus status;

  public final AccessorResponse<T> withResult(T result) {
    this.result = result;

    return this;
  }

  public final AccessorResponse<T> withStatus(AccessorResponseStatus status) {
    this.status = status;

    return this;
  }

  public final AccessorResponse<T> withException(Throwable exception) {
    this.exception = exception;

    return this;
  }

  public final AccessorResponse<T> withHeader(String name, String value) {
    this.headers.put(name, value);

    return this;
  }

  public final AccessorResponse<T> withMetadata(String name, Object value) {
    this.metadata.put(name, value);

    return this;
  }

}
