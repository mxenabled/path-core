package com.mx.accessors;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mx.common.collections.ObjectMap;

/**
 * Named set of {@link AccessorConnection}.
 */
public class AccessorConnections {
  private final Map<String, AccessorConnection> connections = new LinkedHashMap<>();

  public final AccessorConnection getConnection(String name) {
    return connections.get(name);
  }

  public final void addConnection(String name, AccessorConnection connection) {
    connections.put(name, connection);
  }

  public final void describe(ObjectMap description) {
    connections.forEach((clientId, connection) -> {
      connection.describe(description.createMap(clientId));
    });
  }
}
