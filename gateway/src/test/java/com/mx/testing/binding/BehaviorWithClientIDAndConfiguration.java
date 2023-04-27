package com.mx.testing.binding;

import lombok.Getter;

import com.mx.path.core.common.accessor.AccessorResponse;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.configuration.Configuration;
import com.mx.path.core.common.configuration.ConfigurationField;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.configuration.Configurable;
import com.mx.path.gateway.configuration.annotations.ClientID;
import com.mx.path.gateway.context.GatewayRequestContext;

public class BehaviorWithClientIDAndConfiguration extends GatewayBehavior implements Configurable {

  @Getter
  private final String clientId;

  @Getter
  private final BehaviorConfiguration behaviorConfiguration;

  @Getter
  boolean initialized = false;

  public static class BehaviorConfiguration {
    @Getter
    @ConfigurationField("active")
    private boolean active;

    @Getter
    @ConfigurationField("actionFilter")
    private String actionFilter;
  }

  public BehaviorWithClientIDAndConfiguration(ObjectMap configurations, @ClientID String clientId, @Configuration BehaviorConfiguration behaviorConfiguration) {
    super(configurations);
    this.clientId = clientId;
    this.behaviorConfiguration = behaviorConfiguration;
  }

  @Override
  protected <T> AccessorResponse<T> call(Class<T> resultType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return null;
  }

  @Override
  public void initialize() {
    this.initialized = true;
  }
}
