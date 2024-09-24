package com.mx.path.gateway.accessor;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.collection.ObjectMap;

/**
 * Generic response from an accessor.
 *
 * todo: Move back to gateway after model extraction
 * @param <T> type of result returned by accessor
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
  private PathResponseStatus status;

  /**
   * Set accessor response result and return self.
   *
   * @param result accessor response result
   * @return self
   */
  public final AccessorResponse<T> withResult(T result) {
    this.result = result;

    return this;
  }

  /**
   * Set accessor response status and return self.
   *
   * @param status accessor response status
   * @return self
   */
  public final AccessorResponse<T> withStatus(PathResponseStatus status) {
    this.status = status;

    return this;
  }

  /**
   * Set exception thrown during accessor request and return self.
   *
   * @param exception exception thrown during accessor request
   * @return self
   */
  public final AccessorResponse<T> withException(Throwable exception) {
    this.exception = exception;

    return this;
  }

  /**
   * Add new header to accessor response and return self.
   *
   * @param name header name
   * @param value header value
   * @return self
   */
  public final AccessorResponse<T> withHeader(String name, String value) {
    this.headers.put(name, value);

    return this;
  }

  /**
   * Add new metadata to accessor response and return self.
   *
   * @param name metadata name
   * @param value metadata value
   * @return self
   */
  public final AccessorResponse<T> withMetadata(String name, Object value) {
    this.metadata.put(name, value);

    return this;
  }

}
