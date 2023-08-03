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

  public UpstreamRequestConfiguration() {
    forwardedRequestHeaders = new SingleValueMap<>();
    upstreamRequestProcessors = new ArrayList<>();
  }

  /**
   * Headers to be forwarded in all upstream {@link com.mx.path.core.common.connect.Request} executes
   */
  private SingleValueMap<String, Object> forwardedRequestHeaders;

  /**
   * Instances of upstream processors that will be invoked in all upstream {@link com.mx.path.core.common.connect.Request} executes
   */
  private List<UpstreamRequestProcessor> upstreamRequestProcessors;

  public static class UpstreamRequestConfigurationBuilder {
    private SingleValueMap<String, Object> forwardedRequestHeaders = new SingleValueMap<>();
    private List<UpstreamRequestProcessor> upstreamRequestProcessors = new ArrayList<>();

    public final UpstreamRequestConfigurationBuilder forwardedHeader(String key, String value) {
      forwardedRequestHeaders.put(key, value);

      return this;
    }

    public final UpstreamRequestConfigurationBuilder upstreamRequestProcessor(UpstreamRequestProcessor processor) {
      upstreamRequestProcessors.add(processor);

      return this;
    }
  }
}
