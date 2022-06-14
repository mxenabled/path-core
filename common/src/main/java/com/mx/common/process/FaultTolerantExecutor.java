package com.mx.common.process;

/**
 * Task executor with fault-tolerant protections.
 */
public interface FaultTolerantExecutor {

  /**
   * Submits a task to be executed with configurable fault-tolerant protections. The scope parameter allows the
   * facility to select a specific set of configurations for the given task.
   *
   * <p>
   * The scope parameter should be formatted as a dot-delimited string so that a fallback scope can be selected
   * if the fully-qualified scope doesn't have a custom configuration.
   * </p>
   *
   * <p>
   * For example, imagine the following scopes have custom configurations:
   *
   *  - http
   *  - http.accounts
   *  - http.accounts.create
   * </p>
   *
   * <p>
   * Now imagine that `submit(...)` gets called with the scope `http.accounts.list`. There is no custom configuration
   * for `http.accounts.list`, but there is a custom configuration for `http.accounts`. In this case, the `http.accounts`
   * configuration should be selected. Similarly, if the scope `http.profiles.get` is provided, the `http` scope
   * configurations should be selected for the provided task. Finally, if the scope `remote.payouts.update` is provided,
   * none of the custom configurations should be selected and the defaults should be used.
   * </p>
   *
   * @throws FaultTolerantExecutionException on failure.
   *
   * @param scope
   * @param task
   */
  void submit(String scope, FaultTolerantTask task) throws FaultTolerantExecutionException;
}
