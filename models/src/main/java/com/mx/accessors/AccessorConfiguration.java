package com.mx.accessors;

import lombok.Builder;
import lombok.Getter;

import com.mx.common.collections.ObjectMap;

@Getter
@Builder
public class AccessorConfiguration {

  private final String clientId;
  private final ObjectMap configurations;
  private final AccessorConnections connections;

  public static class AccessorConfigurationBuilder {

    private AccessorConnections connections = new AccessorConnections();
    private ObjectMap configurations = new ObjectMap();

    public final AccessorConfigurationBuilder connection(String name, AccessorConnection connection) {
      connections.addConnection(name, connection);

      return this;
    }

    public final AccessorConfigurationBuilder configuration(String key, Object value) {
      configurations.put(key, value);

      return this;
    }

  }

  public final ObjectMap describe() {
    ObjectMap description = new ObjectMap();
    describe(description);

    return description;
  }

  public final void describe(ObjectMap description) {
    description.put("clientId", getClientId());
    connections.describe(description.createMap("connections"));
    ObjectMap configs = description.createMap("configurations");
    configurations.forEach(configs::put);
  }

}
