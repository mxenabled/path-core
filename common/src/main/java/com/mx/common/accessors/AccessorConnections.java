package com.mx.common.accessors;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mx.common.collections.ObjectMap;
import com.mx.common.connect.AccessorConnectionSettings;

/**
 * Named set of {@link AccessorConnectionSettings}.
 * todo: Move back to gateway after model extraction
 */
public class AccessorConnections {
  private final Map<String, AccessorConnectionSettings> connections = new LinkedHashMap<>();

  public final AccessorConnectionSettings getConnection(String name) {
    return connections.get(name);
  }

  public final void addConnection(String name, AccessorConnectionSettings connection) {
    connections.put(name, connection);
  }

  public final void describe(ObjectMap description) {
    connections.forEach((clientId, connection) -> {
      connection.describe(description.createMap(clientId));
    });
  }
}
