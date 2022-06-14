package com.mx.path.gateway.service;

import lombok.Getter;
import lombok.Setter;

import com.mx.common.collections.ObjectMap;
import com.mx.path.gateway.BaseGateway;

public abstract class GatewayService {

  @Getter
  private final ObjectMap configurations;

  @Getter
  @Setter
  private BaseGateway gateway;

  public GatewayService(ObjectMap configurations) {
    this.configurations = configurations;
  }

  public abstract void start();

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

}
