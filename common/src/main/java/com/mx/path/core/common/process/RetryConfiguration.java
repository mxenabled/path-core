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
 * Configuration node that can apply bound configurations to a {@link Retryer} instance.
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
   * Strategy for determining when to stop retrying failed attempts.
   */
  public enum StopStrategy {
    /**
     * Stop by count limit.
     */
    COUNT,
    /**
     * Stop by duration limit.
     */
    DURATION
  }

  /**
   * Strategy for determining pause between failed attempts.
   */
  public enum PauseStrategy {
    /**
     * Fixed.
     */
    FIXED,
    /**
     * Incrementing.
     */
    INCREMENTING,
    /**
     * Fibonacci.
     */
    FIBONACCI
  }

  /**
   * Default constructor.
   */
  public RetryConfiguration() {
  }

  /**
   * Bypass configuration and wrap existing retryer.
   *
   * @param instance retryer instance
   */
  public RetryConfiguration(Retryer instance) {
    this.instance = instance;
  }

  // -----------------------------------
  // Pause settings
  // -----------------------------------

  /**
   * Strategy used to calculate the wait between failed attempts.
   *
   * <p>Default: no pause between failed attempts
   *
   * -- GETTER --
   * Return pause strategy.
   *
   * @return pause strategy
   * -- SETTER --
   * Set pause strategy.
   *
   * @param pauseStrategy pause strategy to set
   */
  @ConfigurationField
  private PauseStrategy pauseStrategy;

  /**
   * The pause duration used between failed attempts.
   *
   * <p>Used by {@link PauseStrategy#FIXED}
   *
   * -- GETTER --
   * Return pause.
   *
   * @return pause
   * -- SETTER --
   * Set pause.
   *
   * @param pause pause to set
   */
  @ConfigurationField
  private Duration pause;

  /**
   * The first pause duration. Incremented by {@link #increment} between all subsequent failures.
   *
   * <p>Used by {@link PauseStrategy#INCREMENTING}
   *
   * -- GETTER --
   * Return first pause duration.
   *
   * @return first pause duration
   * -- SETTER --
   * Set first pause duration.
   *
   * @param initialPause first pause duration to set
   */
  @ConfigurationField
  private Duration initialPause;

  /**
   * The amount the pause duration is increased between failures. {@link #initialPause} is used after the first failure.
   *
   * <p>Used by {@link PauseStrategy#INCREMENTING}
   *
   * -- GETTER --
   * Return increment duration.
   *
   * @return increment duration
   * -- SETTER --
   * Set increment duration.
   *
   * @param increment increment duration to set
   */
  @ConfigurationField
  private Duration increment;

  /**
   * The maximum pause duration.
   *
   * <p>Used by {@link PauseStrategy#FIBONACCI}
   *
   * -- GETTER --
   * Return max pause duration.
   *
   * @return max pause duration
   * -- SETTER --
   * Set max pause duration.
   *
   * @param maxPause max pause duration to set
   */
  @ConfigurationField
  private Duration maxPause;

  /**
   * Duration that is multiplied by the current fibonacci number to get the next pause.
   *
   * <p>Used by {@link PauseStrategy#FIBONACCI}
   *
   * -- GETTER --
   * Return duration increment multiplier.
   *
   * @return duration increment multiplier
   * -- SETTER --
   * Set duration increment multiplier.
   *
   * @param multiplier duration increment multiplier to set
   */
  @ConfigurationField
  private Duration multiplier;

  // -----------------------------------
  // Retry stop settings
  // -----------------------------------
  /**
   * -- GETTER --
   * Return stop strategy.
   *
   * @return stop strategy
   * -- SETTER --
   * Set stop strategy.
   *
   * @param stopStrategy stop strategy to set
   */
  @ConfigurationField(required = true)
  private StopStrategy stopStrategy;

  /**
   * The number of failed attempts before stopping.
   *
   * <p>Used in {@link StopStrategy#COUNT}
   *
   * -- GETTER --
   * Return maximum number of failed attempts.
   *
   * @return maximum number of failed attempts
   * -- SETTER --
   * Set maximum number of failed attempts.
   *
   * @param count maximum number of failed attempts to set
   */
  @ConfigurationField
  private Integer count;

  /**
   * The delay before stopping, starting from first attempt.
   *
   * <p>Used in {@link StopStrategy#DURATION}
   *
   * -- GETTER --
   * Return duration.
   *
   * @return duration
   * -- SETTER --
   * Set duration.
   *
   * @param duration duration to set
   */
  @ConfigurationField
  private Duration duration;

  // -----------------------------------
  // Rejection settings
  // -----------------------------------
  /**
   * -- GETTER --
   * Return reject on.
   *
   * @return reject on
   * -- SETTER --
   * Set reject on.
   *
   * @param rejectOn reject on to set
   */
  private Predicate<T> rejectOn;

  // -----------------------------------
  // Exception settings
  // -----------------------------------

  /**
   * Function that supplies a custom exception to be thrown if all attempts fail.
   *
   * <p>Only affects {@link #call(Callable)}. If {@link #call(Callable, Function)} or {@link #instance()} are used, this
   * has no effect.
   *
   * <p>Note: {@link Retryer} does not support custom exceptions. If {@link #instance()} is called the used,
   * this {@link #exceptionSupplier} will be ignored.
   *
   * -- GETTER --
   * Return exception supplier.
   *
   * @return exception supplier
   * -- SETTER --
   * Set exception supplier.
   *
   * @param exceptionSupplier exception supplier to set
   */
  private transient Function<Throwable, RuntimeException> exceptionSupplier;

  /**
   * This retryer instance.
   *
   * -- GETTER --
   * Return retryer instance.
   *
   * @return retryer instance
   */
  @Getter(AccessLevel.PROTECTED)
  private transient Retryer<T> instance;

  /**
   * Execute callable block using configured retryer.
   *
   * @param callable block to call
   * @return result of first successful attempt
   */
  @SuppressWarnings("PMD.CyclomaticComplexity")
  public T call(Callable<T> callable) {
    return call(callable, this.exceptionSupplier);
  }

  /**
   * Execute callable block using configured retryer.
   *
   * @param callable block to call
   * @param exceptionSupplierOverride supplier
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
   * Get instance of {@link Retryer}.
   *
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
   * Validate settings.
   *
   * @param state state
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
   *
   * @return instance
   */
  protected Retryer<T> buildInstance() {
    return (Retryer<T>) instanceBuilder().build();
  }

  /**
   * Creates instance of {@link RetryerBuilder} and adds configuration. Override to modify the builder before build.
   *
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
