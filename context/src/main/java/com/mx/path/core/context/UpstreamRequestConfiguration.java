package com.mx.path.core.context;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.mx.path.core.common.collection.SingleValueMap;

@Data
@Builder
@AllArgsConstructor
@SuppressWarnings("PMD.UnusedPrivateField")
public class UpstreamRequestConfiguration {

  public UpstreamRequestConfiguration() {
    forwardedRequestHeaders = new SingleValueMap<>();
    upstreamRequestProcessors = new ArrayList<>();
  }

  /**
   * Headers to be forwarded in all upstream requests during current request
   */
  private SingleValueMap<String, Object> forwardedRequestHeaders;

  /**
   * Instances of upstream processors that will be invoked in all upstream calls during current request
   */
  private List<UpstreamRequestProcessor> upstreamRequestProcessors;

  @SuppressWarnings("PMD.UnusedPrivateField")
  public static class UpstreamRequestConfigurationBuilder {
    private SingleValueMap<String, Object> forwardedRequestHeaders = new SingleValueMap<>();
    private List<UpstreamRequestProcessor> upstreamRequestProcessors = new ArrayList<>();

    public final UpstreamRequestConfigurationBuilder forwardedHeader(String key, String value) {
      forwardedRequestHeaders.put(key, value);

      return this;
    }

    public final UpstreamRequestConfigurationBuilder upstreamRequestProcessors(UpstreamRequestProcessor processor) {
      upstreamRequestProcessors.add(processor);

      return this;
    }

    public final UpstreamRequestConfigurationBuilder withForwardedRequestHeaders(Consumer<SingleValueMap<String, Object>> consumer) {
      consumer.accept(forwardedRequestHeaders);

      return this;
    }
  }
}
