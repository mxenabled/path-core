package com.mx.path.core.common.connect;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.mx.path.core.common.configuration.ConfigurationField;
import com.mx.path.gateway.configuration.Configurable;
import com.mx.path.gateway.configuration.ConfigurationError;
import com.mx.path.gateway.configuration.ConfigurationState;

/**
 * Configuration node that can apply bound configurations to a {@link Retryer} instance
 *
 * <p>Set this as the type in configuration POJO to support configurable retries. At runtime, grab the instance and
 * call {@link #instance()}.
 *
 * <p><strong>Fields:</strong>
 * <ul>
 *   <li><strong>{@link #stopStrategy}</strong> - (required) when to give up retrying failed attempts
 *     <ul>
 *       <li>{@link StopStrategy#COUNT} - Stop after fixed number of attempts
 *       <li>{@link StopStrategy#DURATION} - Stop after fixed amount if time
 *     </ul>
 *   <li>{@link #count} - how many attempts (used with {@link StopStrategy#COUNT})
 *   <li>{@link #duration} - how many attempts (used with {@link StopStrategy#DURATION})
 *   <li><strong>{@link #pauseStrategy}</strong> - how long to pause between failed attempts
 *     <ul>
 *       <li>{@link PauseStrategy#FIXED} - pause the same duration between attempts
 *       <li>{@link PauseStrategy#INCREMENTING} - increase the duration the same duration with every attempt
 *       <li>{@link PauseStrategy#FIBONACCI} - increase the duration using the fibonacci sequence as a multiplier with every attempt
 *     </ul>
 *   <li>{@link #pause} - how long to pause (used with {@link PauseStrategy#FIXED})
 *   <li>{@link #initialPause} - how long to pause (used with {@link PauseStrategy#INCREMENTING})
 *   <li>{@link #increment} - amount to increase between failed attempts (used with {@link PauseStrategy#INCREMENTING})
 *   <li>{@link #multiplier} - multiplied by fibonacci number to get pause (used with {@link PauseStrategy#FIBONACCI})
 *   <li>{@link #maxPause} - maximum duration to pause (used with {@link PauseStrategy#FIBONACCI})
 * </ul>
 *
 * <p><strong>Example:</strong>
 *
 * <pre>{@code
 *
 * // Configuration POJO
 * @Data
 * public class FakeBankConnectionConfiguration {
 *   @ConfigurationField(required = true)
 *   private RetryConfiguration accountsRetry;
 * }
 *
 * // Connection class (uses configuration POJO)
 * public class FakeBankConnection extends HttpConnection {
 *   private FakeBankConnectionConfiguration config;
 *
 *   public FakeBankConnection(@Configuration FakeBankConnectionConfiguration config) {
 *     this.config = config;
 *   }
 *
 *   public final List<FakeAccount> loadAccounts(String memberId) {
 *     return request("/" + memberId + "/accountSvc")
 *       .withRetryConfiguration(configs.getAccountsRetry())
 *       .withProcess((response) -> buildResponse(response))
 *       .get()
 *       .throwException();
 *   }
 * }
 *
 * // gateway.yaml (provides values to configuration POJO
 * ...
 * accessor:
 *   class: FakeAccessor
 *   connections:
 *     fakeBank:
 *       configurations:
 *         accountsRetry:
 *           pauseStrategy: fibonacci
 *           multiplier: 100,s
 *           maxPause: 2s
 *
 * }</pre>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryConfiguration implements Configurable {

  /**
   * Strategy for determining when to stop retrying failed attempts
   */
  public enum StopStrategy {
    COUNT,
    DURATION
  }

  /**
   * Strategy for determining pause between failed attempts
   */
  public enum PauseStrategy {
    FIXED,
    INCREMENTING,
    FIBONACCI
  }

  /**
   * Build the default instance.
   *
   * @return non-retying instance
   */
  public static RetryConfiguration defaults() {
    return builder()
        .stopStrategy(StopStrategy.COUNT)
        .count(1)
        .build();
  }

  /**
   * Bypass configuration and wrap existing retryer
   * @param instance
   */
  public RetryConfiguration(Retryer instance) {
    this.instance = instance;
  }

  // -----------------------------------
  // Pause settings
  // -----------------------------------

  /**
   * Strategy used to calculate the wait between failed attempts
   *
   * <p>Default: no pause between failed attempts
   */
  @ConfigurationField
  private PauseStrategy pauseStrategy;

  /**
   * The pause duration used between failed attempts
   *
   * <p>Used by {@link PauseStrategy#FIXED}
   */
  @ConfigurationField
  private Duration pause;

  /**
   * The first pause duration. Incremented by {@link #increment} between all subsequent failures.
   *
   * <p>Used by {@link PauseStrategy#INCREMENTING}
   */
  @ConfigurationField
  private Duration initialPause;

  /**
   * The amount the pause duration is increased between failures. {@link #initialPause} is used after the first failure.
   *
   * <p>Used by {@link PauseStrategy#INCREMENTING}
   */
  @ConfigurationField
  private Duration increment;

  /**
   * The maximum pause duration
   *
   * <p>Used by {@link PauseStrategy#FIBONACCI}
   */
  @ConfigurationField
  private Duration maxPause;

  /**
   * Duration that is multiplied by the current fibonacci number to get the next pause
   *
   * <p>Used by {@link PauseStrategy#FIBONACCI}
   */
  @ConfigurationField
  private Duration multiplier;

  // -----------------------------------
  // Retry stop settings
  // -----------------------------------
  @ConfigurationField(required = true)
  private StopStrategy stopStrategy;

  /**
   * The number of failed attempts before stopping
   *
   * <p>Used in {@link StopStrategy#COUNT}
   */
  @ConfigurationField
  private Integer count;

  /**
   * The delay before stopping, starting from first attempt
   *
   * <p>Used in {@link StopStrategy#DURATION}
   */
  @ConfigurationField
  private Duration duration;

  // -----------------------------------
  // Rejection settings
  // -----------------------------------
  @ConfigurationField(elementType = ResponseMatcher.class)
  @Builder.Default
  private List<ResponseMatcher> retryOn = new ArrayList<>();

  @Getter
  @Setter
  private transient Function<Throwable, Throwable> exceptionSupplier;

  private transient Retryer<? extends Response<?, ?>> instance;

  /**
   * Execute callable block using configured retryer
   *
   * @param callable block to call
   * @return result of first successful attempt
   * @param <T>
   * @throws ExecutionException thrown when called throws an exception
   * @throws RetryException thrown when no attempts succeed
   */
  @SneakyThrows
  @SuppressWarnings("unchecked")
  public <T extends Response<?, ?>> T call(Callable<T> callable) throws ExecutionException, RetryException {
    try {
      return (T) instance().call((Callable<Response<?, ?>>) callable);
    } catch (ExecutionException | RetryException e) {
      if (exceptionSupplier != null) {
        throw exceptionSupplier.apply(e);
      }

      throw e;
    }
  }

  /**
   * Get instance of {@link Retryer}
   * @return
   * @param <T>
   */
  @SuppressWarnings("unchecked")
  public <T extends Response<?, ?>> Retryer<T> instance() {
    if (instance == null) {
      instance = build();
    }

    return (Retryer<T>) instance;
  }

  /**
   * Validate settings
   *
   * @param state
   */
  @SuppressWarnings("PMD.CyclomaticComplexity")
  @Override
  public void validate(ConfigurationState state) {
    Configurable.super.validate(state);

    List<String> missingFields = new ArrayList<>();

    if (stopStrategy == null) {
      state.field("stopStrategy");
      throw new ConfigurationError("Missing required field", state);
    }

    switch (stopStrategy) {
      case COUNT:
        if (count == null) {
          missingFields.add("count");
        }
        break;
      case DURATION:
        if (duration == null) {
          missingFields.add("duration");
        }
        break;
      default:
        throw new ConfigurationError("Unsupported stopStrategy: " + stopStrategy, state);
    }

    if (!missingFields.isEmpty()) {
      throw new ConfigurationError("Missing required fields for stopStrategy " + stopStrategy + ": " + String.join(", ", missingFields), state);
    }

    // The default is to not wait between failed attempts
    if (pauseStrategy != null) {
      switch (pauseStrategy) {
        case FIXED:
          if (pause == null) {
            missingFields.add("pause");
          }
          break;
        case INCREMENTING:
          if (initialPause == null) {
            missingFields.add("initialPause");
          }
          if (increment == null) {
            missingFields.add("increment");
          }
          break;
        case FIBONACCI:
          if (multiplier == null) {
            missingFields.add("multiplier");
          }
          if (maxPause == null) {
            missingFields.add("maxPause");
          }
          break;
        default:
          throw new ConfigurationError("Unsupported pauseStrategy: " + pauseStrategy, state);
      }

      if (!missingFields.isEmpty()) {
        throw new ConfigurationError("Missing required fields for pauseStrategy " + pauseStrategy + ": " + String.join(", ", missingFields), state);
      }
    }
  }

  private <T extends Response<?, ?>> Retryer<T> build() {
    RetryerBuilder<T> builder = RetryerBuilder.newBuilder();
    if (stopStrategy == StopStrategy.COUNT) {
      builder.withStopStrategy(StopStrategies.stopAfterAttempt(count));
    }
    if (stopStrategy == StopStrategy.DURATION) {
      builder.withStopStrategy(StopStrategies.stopAfterDelay(duration.toMillis(), TimeUnit.MILLISECONDS));
    }

    if (pauseStrategy == PauseStrategy.FIXED) {
      builder.withWaitStrategy(WaitStrategies.fixedWait(pause.toMillis(), TimeUnit.MILLISECONDS));
    }
    if (pauseStrategy == PauseStrategy.INCREMENTING) {
      builder.withWaitStrategy(WaitStrategies.incrementingWait(initialPause.toMillis(), TimeUnit.MILLISECONDS, increment.toMillis(), TimeUnit.MILLISECONDS));
    }
    if (pauseStrategy == PauseStrategy.FIBONACCI) {
      builder.withWaitStrategy(WaitStrategies.fibonacciWait(multiplier.toMillis(), maxPause.toMillis(), TimeUnit.MILLISECONDS));
    }

    retryOn.forEach((retryRule) -> {
      builder.retryIfResult(retryRule::test);
    });

    return builder.build();
  }
}
