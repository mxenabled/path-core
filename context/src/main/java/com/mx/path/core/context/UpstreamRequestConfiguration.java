package com.mx.path.core.context;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.mx.path.core.common.collection.SingleValueMap;

/**
 * Configuration to be applied to all upstream requests within {@link RequestContext#getUpstreamRequestConfiguration()}
 */
@Data
@Builder
@AllArgsConstructor
public class UpstreamRequestConfiguration {

  /**
   * Build new instance of {@link UpstreamRequestConfiguration}.
   */
  public UpstreamRequestConfiguration() {
    forwardedRequestHeaders = new SingleValueMap<>();
    upstreamRequestProcessors = new ArrayList<>();
  }

  /**
   * Headers to be forwarded in all upstream {@link com.mx.path.core.common.connect.Request} executes.
   *
   * -- GETTER --
   * Return request headers.
   *
   * @return request headers
   * -- SETTER --
   * Set request headers.
   *
   * @param forwardedRequestHeaders request headers to set
   */
  private SingleValueMap<String, Object> forwardedRequestHeaders;

  /**
   * Instances of upstream processors that will be invoked in all upstream {@link com.mx.path.core.common.connect.Request} executes.
   *
   * -- GETTER --
   * Return request processors.
   *
   * @return request processors
   * -- SETTER --
   * Set request processors.
   *
   * @param upstreamRequestProcessors request processors to set
   */
  private List<UpstreamRequestProcessor> upstreamRequestProcessors;

  /**
   * Builder for {@link UpstreamRequestConfiguration}.
   */
  public static class UpstreamRequestConfigurationBuilder {
    private SingleValueMap<String, Object> forwardedRequestHeaders = new SingleValueMap<>();
    private List<UpstreamRequestProcessor> upstreamRequestProcessors = new ArrayList<>();

    /**
     * Add new header.
     *
     * @param key key
     * @param value value
     * @return self
     */
    public final UpstreamRequestConfigurationBuilder forwardedHeader(String key, String value) {
      forwardedRequestHeaders.put(key, value);

      return this;
    }

    /**
     * Add new processor
     *
     * @param processor processor to add
     * @return self
     */
    public final UpstreamRequestConfigurationBuilder upstreamRequestProcessor(UpstreamRequestProcessor processor) {
      upstreamRequestProcessors.add(processor);

      return this;
    }
  }
}
