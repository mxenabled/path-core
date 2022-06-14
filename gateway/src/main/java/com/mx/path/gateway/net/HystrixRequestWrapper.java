package com.mx.path.gateway.net;

import java.util.function.Supplier;

import com.mx.common.http.HttpStatus;
import com.mx.common.request.Feature;
import com.mx.path.gateway.util.MdxApiException;
import com.netflix.hystrix.HystrixCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HystrixRequestWrapper<T> extends HystrixCommand<T> {
  private Supplier<T> execFunc;
  private static final Logger LOGGER = LoggerFactory.getLogger(HystrixRequestWrapper.class);

  @Deprecated
  public HystrixRequestWrapper(Supplier<T> function, int requestTimeOutInMilli, String featureName) {
    super(HystrixConfigurations.buildHystrixSetterConfiguration(requestTimeOutInMilli, featureName));
    this.execFunc = function;
  }

  public HystrixRequestWrapper(Supplier<T> function, int requestTimeOutInMilli, Feature feature) {
    super(HystrixConfigurations.buildHystrixSetterConfiguration(requestTimeOutInMilli, feature));
    this.execFunc = function;
  }

  @Override
  protected final T run() throws RuntimeException {
    return execFunc.get();
  }

  @Override
  protected final T getFallback() {
    Throwable exception = getFailedExecutionException();
    LOGGER.error(exception.getMessage(), exception);
    throw new MdxApiException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false, exception);
  }
}
