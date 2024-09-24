package com.mx.path.gateway.accessor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.connect.AccessorConnectionSettings;

/**
 * Named set of {@link AccessorConnectionSettings}.
 *
 * todo: Move back to gateway after model extraction
 */
public class AccessorConnections {
  private final Map<String, AccessorConnectionSettings> connections = new LinkedHashMap<>();

  /**
   * Get connection settings for given name.
   *
   * @param name connection name
   * @return connection settings for the given name
   */
  public final AccessorConnectionSettings getConnection(String name) {
    return connections.get(name);
  }

  /**
   * Add new connection settings to map.
   *
   * @param name name of connection settings
   * @param connection connection settings
   */
  public final void addConnection(String name, AccessorConnectionSettings connection) {
    connections.put(name, connection);
  }

  /**
   * Fill {@link ObjectMap} with connection description.
   *
   * @param description object to fill with connection description
   */
  public final void describe(ObjectMap description) {
    connections.forEach((clientId, connection) -> {
      connection.describe(description.createMap(clientId));
    });
  }
}
