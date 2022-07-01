package com.mx.path.gateway.net;

import java.util.Objects;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.mx.common.request.Feature;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * Hystrix configs -> https://github.com/Netflix/Hystrix/wiki/Configuration
 *
 * #example config
 * hystrix:
 *  enable: true
 *  group_name: Servicename
 *  timeout_in_ms: 20000
 *  queue:
 *    enable: true
 *    max_queue_size: 1
 *    max_core_size: 1
 *    queue_size_rejection_threshold: 1
 *  circuit_breaker:
 *    enable: true
 *    sleep_window_in_ms: 5000
 *    request_volume_threshold: 1
 *
 *    timeoutInMs - time in ms after which the caller will observe a timeout and walk away from the command execution
 *    maxQueueSize - maximum queue size of the LinkedBlockingQueue
 *    maxCoreSize - core thread-pool size
 *    queueSizeRejectionThreshold - artificial maximum queue size at which rejections will occur even if maxQueueSize has not been reached
 *    sleepWindowInMs - the amount of time, after tripping the circuit, to reject requests before allowing attempts again to determine if the circuit should again be closed
 *    requestVolumeThreshold - the minimum number of requests in a rolling window that will trip the circuit
 */
@SuppressFBWarnings
@Deprecated
public class HystrixConfigurations {
  private static final int DEFAULT_TIMEOUT_IN_MS = 10000;
  private static final int DEFAULT_MAX_QUEUE_SIZE = -1;
  private static final int DEFAULT_MAX_CORE_SIZE = 16;
  private static final int DEFAULT_QUEUE_SIZE_REJECTION_THRESHOLD = -1;
  private static final int DEFAULT_SLEEP_WINDOW_IN_MS = 5000;
  private static final int DEFAULT_REQUEST_VOLUME_THRESHOLD = 3;
  private static final boolean DEFAULT_ENABLE_CIRCUIT_BREAKER = true;
  private static final boolean DEFAULT_ENABLE_HYSTRIX_WRAPPER = false;

  private static String groupName = "default";
  private static int timeoutInMs = DEFAULT_TIMEOUT_IN_MS;
  private static int maxQueueSize = DEFAULT_MAX_QUEUE_SIZE;
  private static int maxCoreSize = DEFAULT_MAX_CORE_SIZE;
  private static int queueSizeRejectionThreshold = DEFAULT_QUEUE_SIZE_REJECTION_THRESHOLD;
  private static int sleepWindowInMs = DEFAULT_SLEEP_WINDOW_IN_MS;
  private static int requestVolumeThreshold = DEFAULT_REQUEST_VOLUME_THRESHOLD;
  private static boolean enableCircuitBreaker = DEFAULT_ENABLE_CIRCUIT_BREAKER;
  private static boolean enableHystrixWrapper = DEFAULT_ENABLE_HYSTRIX_WRAPPER;

  public static String getGroupName() {
    return groupName;
  }

  public static void setGroupName(String groupName) {
    HystrixConfigurations.groupName = groupName;
  }

  public static int getTimeoutInMs() {
    return timeoutInMs;
  }

  // Thread Pool configuration

  public static void setTimeoutInMs(int timeoutInMs) {
    HystrixConfigurations.timeoutInMs = timeoutInMs;
  }

  public static int getMaxQueueSize() {
    return maxQueueSize;
  }

  public static void setMaxQueueSize(int maxQueueSize) {
    HystrixConfigurations.maxQueueSize = maxQueueSize;
  }

  public static int getMaxCoreSize() {
    return maxCoreSize;
  }

  public static void setMaxCoreSize(int maxCoreSize) {
    HystrixConfigurations.maxCoreSize = maxCoreSize;
  }

  public static int getQueueSizeRejectionThreshold() {
    return queueSizeRejectionThreshold;
  }

  public static void setQueueSizeRejectionThreshold(int queueSizeRejectionThreshold) {
    HystrixConfigurations.queueSizeRejectionThreshold = queueSizeRejectionThreshold;
  }

  // Circuit Breaker Configuration

  public static int getSleepWindowInMs() {
    return sleepWindowInMs;
  }

  public static void setSleepWindowInMs(int sleepWindowInMs) {
    HystrixConfigurations.sleepWindowInMs = sleepWindowInMs;
  }

  public static int getRequestVolumeThreshold() {
    return requestVolumeThreshold;
  }

  public static void setRequestVolumeThreshold(int requestVolumeThreshold) {
    HystrixConfigurations.requestVolumeThreshold = requestVolumeThreshold;
  }

  public static boolean isEnableCircuitBreaker() {
    return enableCircuitBreaker;
  }

  public static void setEnableCircuitBreaker(boolean enableCircuitBreaker) {
    HystrixConfigurations.enableCircuitBreaker = enableCircuitBreaker;
  }

  public static boolean isEnableHystrixWrapper() {
    return enableHystrixWrapper;
  }

  public static void setEnableHystrixWrapper(boolean enableHystrixWrapper) {
    HystrixConfigurations.enableHystrixWrapper = enableHystrixWrapper;
  }

  @Deprecated
  public static HystrixCommand.Setter buildHystrixSetterConfiguration(int timeoutInMilli, String featureName) {
    //Setting the Group name from the properties file
    HystrixCommand.Setter config = HystrixCommand.Setter
        .withGroupKey(HystrixCommandGroupKey.Factory.asKey(getGroupName()));
    //adding the endpoint name to have config specific to per featureName( bundled endpoints together for certain feature)
    if (featureName != null && !featureName.isEmpty()) {
      config.andCommandKey(HystrixCommandKey.Factory.asKey(featureName));
    }

    HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();

    //Hystrix timeout for each request
    commandProperties.withExecutionTimeoutInMilliseconds(timeoutInMilli);

    //Configuration for no of threads at any point of time that hystrix should spin up
    //Max queue size -> no of request it can have in the queue
    //Core Size ->no of threads that stay alive in the given context
    //QueueSize Rejection Threshold -> the limit where the rejection of the calls start happening when there are that no of calls already in queue.
    config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
        .withMaxQueueSize(getMaxQueueSize())
        .withCoreSize(getMaxCoreSize())
        .withQueueSizeRejectionThreshold(getQueueSizeRejectionThreshold()));

    //Sleep time between the requests
    commandProperties.withCircuitBreakerSleepWindowInMilliseconds(getSleepWindowInMs());
    commandProperties.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
    //ENABLING THE CIRCUIT BREAKER
    commandProperties.withCircuitBreakerEnabled(isEnableCircuitBreaker());
    commandProperties.withCircuitBreakerRequestVolumeThreshold(getRequestVolumeThreshold());

    config.andCommandPropertiesDefaults(commandProperties);

    return config;
  }

  public static HystrixCommand.Setter buildHystrixSetterConfiguration(int timeoutInMilli, Feature feature) {
    //Setting the Group name from the properties file
    HystrixCommand.Setter config = HystrixCommand.Setter
        .withGroupKey(HystrixCommandGroupKey.Factory.asKey(getGroupName()));
    //adding the endpoint name to have config specific to per featureName( bundled endpoints together for certain feature)
    if (!Objects.isNull(feature)) {
      config.andCommandKey(HystrixCommandKey.Factory.asKey(feature.toString()));
    }

    HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();

    //Hystrix timeout for each request
    commandProperties.withExecutionTimeoutInMilliseconds(timeoutInMilli);

    //Configuration for no of threads at any point of time that hystrix should spin up
    //Max queue size -> no of request it can have in the queue
    //Core Size ->no of threads that stay alive in the given context
    //QueueSize Rejection Threshold -> the limit where the rejection of the calls start happening when there are that no of calls already in queue.
    config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
        .withMaxQueueSize(getMaxQueueSize())
        .withCoreSize(getMaxCoreSize())
        .withQueueSizeRejectionThreshold(getQueueSizeRejectionThreshold()));

    //Sleep time between the requests
    commandProperties.withCircuitBreakerSleepWindowInMilliseconds(getSleepWindowInMs());
    commandProperties.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
    //ENABLING THE CIRCUIT BREAKER
    commandProperties.withCircuitBreakerEnabled(isEnableCircuitBreaker());
    commandProperties.withCircuitBreakerRequestVolumeThreshold(getRequestVolumeThreshold());

    config.andCommandPropertiesDefaults(commandProperties);

    return config;
  }
}
