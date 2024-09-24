package com.mx.path.core.common.process;

import java.time.Duration;

import lombok.Data;

/**
 * Contains useful data about the conditions that a {@link FaultTolerantTask} is being run with.
 */
@Data
public class FaultTolerantScopeConfiguration {

  /**
   * -- GETTER --
   * Return tolerance limit.
   *
   * @return tolerance limit
   * -- SETTER --
   * Set tolerance limit.
   *
   * @param timeout tolerance limit to set
   */
  private Duration timeout;
}
