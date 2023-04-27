package com.mx.path.gateway.accessor;

import lombok.Builder;
import lombok.Getter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.connect.AccessorConnectionSettings;

/**
 * todo: Move back to gateway after model extraction
 */
@Getter
@Builder
public class AccessorConfiguration {

  private final String clientId;
  private final ObjectMap configurations;
  private final AccessorConnections connections;

  public static class AccessorConfigurationBuilder {

    private AccessorConnections connections = new AccessorConnections();
    private ObjectMap configurations = new ObjectMap();

    public final AccessorConfigurationBuilder connection(String name, AccessorConnectionSettings connection) {
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
