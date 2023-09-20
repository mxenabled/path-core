package com.mx.path.core.common.process;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

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
 * <p>See {@link BlockRetryConfiguration}
 * <p>See {@link com.mx.path.core.common.connect.ResponseRetryConfiguration}
 */
@Data
@SuperBuilder
public abstract class RetryConfiguration<T> implements Configurable {

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

  public RetryConfiguration() {
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
  private Predicate<T> rejectOn;

  // -----------------------------------
  // Exception settings
  // -----------------------------------

  /**
   * Function that supplies a custom exception to be thrown if all attempts fail
   *
   * <p>Only affects {@link #call(Callable)}. If {@link #call(Callable, Function)} or {@link #instance()} are used, this
   * has no effect.
   *
   * <p>Note: {@link Retryer} does not support custom exceptions. If {@link #instance()} is called the used,
   * this {@link #exceptionSupplier} will be ignored.
   */
  private transient Function<Throwable, RuntimeException> exceptionSupplier;

  @Getter(AccessLevel.PROTECTED)
  private transient Retryer<T> instance;

  /**
   * Execute callable block using configured retryer
   *
   * @param callable block to call
   * @return result of first successful attempt
   */
  @SuppressWarnings("PMD.CyclomaticComplexity")
  public T call(Callable<T> callable) {
    return call(callable, this.exceptionSupplier);
  }

  /**
   * Execute callable block using configured retryer
   *
   * @param callable block to call
   * @return result of first successful attempt
   */
  @SuppressWarnings("PMD.CyclomaticComplexity")
  public T call(Callable<T> callable, Function<Throwable, RuntimeException> exceptionSupplierOverride) {
    try {
      return (T) instance().call(callable);
    } catch (RetryException e) {
      if (exceptionSupplierOverride != null) {
        throw exceptionSupplierOverride.apply(e);
      }

      throw new RetriesFailedException(e).withNumberOfFailedAttempts(e.getNumberOfFailedAttempts());
    } catch (ExecutionException e) {
      if (e.getCause() != null) {
        if (e.getCause() instanceof RuntimeException) {
          throw (RuntimeException) e.getCause();
        }

        throw new RuntimeException(e.getCause());
      }

      // Last chance. Not sure if this will ever happen. Docs say that exceptions thrown will be wrapped in ExecutionException
      throw new RuntimeException(e);
    }
  }

  /**
   * Get instance of {@link Retryer}
   * @return instance
   */
  @SuppressWarnings("unchecked")
  public Retryer<T> instance() {
    if (instance == null) {
      instance = buildInstance();
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

  /**
   * Builds instance of {@link Retryer}. Override to add to build logic.
   * @return
   */
  protected Retryer<T> buildInstance() {
    return (Retryer<T>) instanceBuilder().build();
  }

  /**
   * Creates instance of {@link RetryerBuilder} and adds configuration. Override to modify the builder before build.
   * @return instance of {@link RetryerBuilder}
   */
  protected RetryerBuilder<T> instanceBuilder() {
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

    if (rejectOn != null) {
      builder.retryIfResult(rejectOn::test);
    }

    return builder;
  }
}
