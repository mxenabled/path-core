package com.mx.path.core.common.process;

import lombok.experimental.SuperBuilder;

/**
 * Used to retry an arbitrary block of code according to configuration values.
 *
 * <p>See {@link RetryConfiguration} for base behavior.
 * <p>For Request retries, see {@link com.mx.path.core.common.connect.ResponseRetryConfiguration}
 *
 * <p><strong>Example:</strong>
 *
 * <pre>{@code
 *
 * // Configuration POJO
 * @Data
 * public class FakeBankConnectionConfiguration {
 *   @ConfigurationField(required = true)
 *   private BlockRetryConfiguration<Boolean> pingRetry;
 * }
 *
 * // Connection class (uses configuration POJO)
 * public class FakeBankConnection extends HttpConnection {
 *   private FakeBankConnectionConfiguration config;
 *
 *   public FakeBankConnection(@Configuration FakeBankConnectionConfiguration config) {
 *     this.config = config;
 *
 *     // Provide rejection predicate
 *     config.pingRetry().rejectOn((result) -> {
 *       if (result) {
 *         return false; // succeeded, don't retry
 *       }
 *       return true; // retry
 *     });
 *   }
 *
 *   public final void pingSystem() {
 *     // Use configured pingRetry to wrap an arbitrary block of code
 *
 *     configs.pingRetry().call(() -> {
 *       System.println("Pinging system");
 *       bool systemAlive = false;
 *
 *       // Do stuff
 *       // ...
 *
 *       return systemAlive; // true = success, false = system down
 *     });
 *   }
 * }
 *
 * // gateway.yaml (provides values to configuration POJO
 * ...
 * accessor:
 *   class: FakeAccessor
 *   configurations:
 *     accountsRetry:
 *       stopStrategy: count
 *       count: 3
 *       pauseStrategy: fibonacci
 *       multiplier: 100ms
 *       maxPause: 2s
 *
 * }</pre>
 *
 * @param <T> result of retry block
 */
@SuperBuilder
public class BlockRetryConfiguration<T> extends RetryConfiguration<T> {
}
