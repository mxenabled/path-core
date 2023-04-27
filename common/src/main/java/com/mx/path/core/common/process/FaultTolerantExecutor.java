package com.mx.path.core.common.process;

/**
 * Task executor with fault-tolerant protections.
 */
public interface FaultTolerantExecutor {

  /**
   * Submits a task to be executed with configurable fault-tolerant protections. The scope parameter allows the
   * facility to select a specific set of configurations for the given task.
   *
   * <p>The scope parameter should be formatted as a dot-delimited string so that a fallback scope can be selected
   * if the fully-qualified scope doesn't have a custom configuration.
   *
   * <p>For example, imagine the following scopes have custom configurations:
   *
   * <ul>
   *   <li>http
   *   <li>http.accounts
   *   <li>http.accounts.create
   * </ul>
   *
   * <p>Now imagine that {@code submit(...)} gets called with the scope {@code http.accounts.list}. There is no custom configuration
   * for {@code http.accounts.list}, but there is a custom configuration for {@code http.accounts}. In this case, the {@code http.accounts}
   * configuration should be selected. Similarly, if the scope {@code http.profiles.get} is provided, the {@code http} scope
   * configurations should be selected for the provided task. Finally, if the scope {@code remote.payouts.update} is provided,
   * none of the custom configurations should be selected and the defaults should be used.
   *
   * @param scope
   * @param task
   */
  void submit(String scope, FaultTolerantTask task);
}
