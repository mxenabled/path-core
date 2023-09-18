package com.mx.path.core.common.connect;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.mx.path.core.common.configuration.ConfigurationField;
import com.mx.path.core.common.process.RetryConfiguration;

/**
 * Used to retry requests. Responses can be matched using {@link #onResponse} to determine if request should be retried.
 *
 * <p>See {@link RetryConfiguration} for base behavior.
 * <p>For arbitrary block retries, see {@link com.mx.path.core.common.process.BlockRetryConfiguration}
 *
 * <p><strong>Configuration:</strong>
 * <p>Uses all configurations from @{@link com.mx.path.core.common.process.BlockRetryConfiguration} and adds the following:
 *
 * <ul>
 *   <li>{@link #onResponse} - Describes matching rules for retriable {@link Response}. See {@link ResponseMatcher}
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
 *           stopStrategy: duration
 *           duration: 10s
 *           pauseStrategy: fibonacci
 *           multiplier: 100ms
 *           maxPause: 2s
 *           onResponse:
 *           - status: ACCEPTED
 *
 * }</pre>
 *
 * @param <RESP>> Type of {@link Response}
 */
@Data
@SuperBuilder
public class ResponseRetryConfiguration<RESP extends Response<?, ?>> extends RetryConfiguration<RESP> {
  /**
   * Bypass configuration and wrap existing retryer
   * @param instance
   */
  public ResponseRetryConfiguration(Retryer<?> instance) {
    super(instance);
  }

  public ResponseRetryConfiguration() {
  }

  /**
   * Build the default instance.
   *
   * @return non-retying instance
   */
  public static <T extends Response<?, ?>> ResponseRetryConfiguration<T> defaults() {
    return (ResponseRetryConfiguration<T>) ResponseRetryConfiguration.<T>builder()
        .stopStrategy(StopStrategy.COUNT)
        .count(1)
        .build();
  }

  // -----------------------------------
  // Rejection settings
  // -----------------------------------
  @Getter
  @ConfigurationField(elementType = ResponseMatcher.class)
  private List<ResponseMatcher<Response<?, ?>>> onResponse;

  @Override
  protected final RetryerBuilder<RESP> instanceBuilder() {
    if (onResponse != null) {
      onResponse.forEach((t) -> {
        if (getRejectOn() == null) {
          setRejectOn(t::test);
        } else {
          setRejectOn(getRejectOn().or(t::test));
        }
      });
    }

    return super.instanceBuilder();
  }
}
