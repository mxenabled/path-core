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

  /**
   * Accessor client id.
   *
   * -- GETTER --
   * Return client id.
   *
   * @return client id
   */
  private final String clientId;

  /**
   * Accessor configuration.
   *
   * -- GETTER --
   * Return accessor configuration.
   *
   * @return accessor configuration
   */
  private final ObjectMap configurations;

  /**
   * Accessor connections.
   *
   * -- GETTER --
   * Return accessor connections.
   *
   * @return accessor connections
   */
  private final AccessorConnections connections;

  /**
   * Helper class to build accessor configuration.
   */
  public static class AccessorConfigurationBuilder {

    /**
     * Accessor connections.
     *
     * -- GETTER --
     * Return accessor connections.
     *
     * @return accessor connections
     */
    private AccessorConnections connections = new AccessorConnections();

    /**
     * Accessor configurations.
     *
     * -- GETTER --
     * Return accessor configurations.
     *
     * @return accessor configurations
     */
    private ObjectMap configurations = new ObjectMap();

    /**
     * Add new connection settings to accessor.
     *
     * @param name settings name
     * @param connection settings to add
     * @return self
     */
    public final AccessorConfigurationBuilder connection(String name, AccessorConnectionSettings connection) {
      connections.addConnection(name, connection);

      return this;
    }

    /**
     * Add new configuration to accessor.
     *
     * @param key configuration map key
     * @param value configuration
     * @return self
     */
    public final AccessorConfigurationBuilder configuration(String key, Object value) {
      configurations.put(key, value);

      return this;
    }

  }

  /**
   * Return map with connections and configurations description.
   *
   * @return descriptions
   */
  public final ObjectMap describe() {
    ObjectMap description = new ObjectMap();
    describe(description);

    return description;
  }

  /**
   * Fill {@link ObjectMap} with descriptions
   *
   * @param description object to fill
   */
  public final void describe(ObjectMap description) {
    description.put("clientId", getClientId());
    connections.describe(description.createMap("connections"));
    ObjectMap configs = description.createMap("configurations");
    configurations.forEach(configs::put);
  }

}
