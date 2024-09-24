package com.mx.path.gateway.service;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.gateway.Gateway;

/**
 * Gateway service.
 */
public abstract class GatewayService {

  @Getter
  private final ObjectMap configurations;

  @Setter
  private Gateway gateway;

  /**
   * Build new {@link GatewayService} instance with specified configurations.
   *
   * @param configurations configurations
   */
  public GatewayService(ObjectMap configurations) {
    this.configurations = configurations;
  }

  /**
   * Start service.
   */
  public abstract void start();

  /**
   * Stop service.
   */
  public abstract void stop();

  /**
   * @return description for this service
   */
  public ObjectMap describe() {
    ObjectMap description = new ObjectMap();
    describe(description);

    return description;
  }

  /**
   * Add this service's description
   * @param description to modify
   */
  public void describe(ObjectMap description) {
    description.put("class", getClass().getSimpleName());

    if (configurations != null && !configurations.isEmpty()) {
      description.put("configurations", configurations);
    }
  }

  /**
   * @return return gateway
   * @param <T> type
   */
  @SuppressWarnings("unchecked")
  public final <T extends Gateway> T getGateway() {
    return (T) gateway;
  }
}
