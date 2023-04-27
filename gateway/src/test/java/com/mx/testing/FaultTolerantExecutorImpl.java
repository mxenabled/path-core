package com.mx.testing;

import lombok.Data;
import lombok.Getter;

import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.core.common.configuration.ConfigurationField;
import com.mx.path.core.common.process.FaultTolerantExecutor;
import com.mx.path.core.common.process.FaultTolerantScopeConfiguration;
import com.mx.path.core.common.process.FaultTolerantTask;
import com.mx.path.gateway.configuration.annotations.ClientID;

public class FaultTolerantExecutorImpl implements FaultTolerantExecutor {
  @Data
  public static class Config {
    @ClientID
    private String clientId;

    @ConfigurationField
    private String timeoutScope;
  }

  @Getter
  private Config config;

  public FaultTolerantExecutorImpl(@Configuration Config config) {
    this.config = config;
  }

  @Override
  public void submit(String scope, FaultTolerantTask task) {
    task.apply(new FaultTolerantScopeConfiguration());
  }
}
