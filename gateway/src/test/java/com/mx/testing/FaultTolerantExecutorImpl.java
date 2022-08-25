package com.mx.testing;

import lombok.Data;
import lombok.Getter;

import com.mx.common.configuration.Configuration;
import com.mx.common.configuration.ConfigurationField;
import com.mx.common.process.FaultTolerantExecutor;
import com.mx.common.process.FaultTolerantScopeConfiguration;
import com.mx.common.process.FaultTolerantTask;
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
