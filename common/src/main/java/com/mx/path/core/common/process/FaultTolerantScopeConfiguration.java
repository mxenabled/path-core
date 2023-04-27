package com.mx.path.core.common.process;

import java.time.Duration;

import lombok.Data;

/**
 * Contains useful data about the conditions that a {@link FaultTolerantTask} is being run with.
 */
@Data
public class FaultTolerantScopeConfiguration {
  private Duration timeout;
}
