package com.mx.common.process;

import java.util.function.Function;

/**
 * A `FaultTolerantTask` is a function that takes in a {@link FaultTolerantScopeConfiguration} and produces no
 * result. This allows the caller to inspect the fault-tolerant conditions that the task is being run with
 * and make decisions accordingly if desired.
 */
public interface FaultTolerantTask extends Function<FaultTolerantScopeConfiguration, Void> {

}
