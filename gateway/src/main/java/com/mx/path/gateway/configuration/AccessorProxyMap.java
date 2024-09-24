package com.mx.path.gateway.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.mx.path.core.common.gateway.GatewayException;
import com.mx.path.gateway.accessor.Accessor;
import com.mx.path.gateway.configuration.annotations.AccessorScope;

/**
 * Manages accessor and  accessor proxy mapping.
 */
public class AccessorProxyMap {

  private static Map<String, Map<Class<? extends Accessor>, Class<? extends Accessor>>> accessorProxyMap = new HashMap<>();

  /**
   * Clear {@code accessorProxyMap}.
   */
  static void reset() {
    accessorProxyMap = new HashMap<>();
  }

  /**
   * Add new element to accessor and accessor proxy map.
   *
   * @param scopeName name of scope
   * @param accessorBaseClass base class of map
   * @param accessorProxyClass accessor proxy of map
   */
  public static void add(String scopeName, Class<? extends Accessor> accessorBaseClass, Class<? extends Accessor> accessorProxyClass) {
    scopeName = scopeName.toLowerCase(Locale.ROOT);
    try {
      accessorProxyMap.computeIfAbsent(scopeName, (k) -> new HashMap<>());
      accessorProxyMap.get(scopeName).put(accessorBaseClass, accessorProxyClass);
    } catch (UnsupportedOperationException e) {
      throw new GatewayException("Attempting to modify frozen Accessor Proxy Mappings", e);
    }
  }

  /**
   * Set accessor proxy to immutable state.
   */
  public static void freeze() {
    Arrays.asList(AccessorScope.values()).forEach((scope) -> {
      String scopeName = scope.getName().toLowerCase(Locale.ROOT);
      if (accessorProxyMap.get(scopeName) != null) {
        accessorProxyMap.put(scopeName, Collections.unmodifiableMap(accessorProxyMap.get(scopeName)));
      }
    });
    accessorProxyMap = Collections.unmodifiableMap(accessorProxyMap);
  }

  /**
   * Find proxy for given scope name and class type.
   *
   * @param scopeName scope name
   * @param klass class type
   * @return proxy class for given parameters
   */
  public static Class<?> get(String scopeName, Class<? extends Accessor> klass) {
    scopeName = scopeName.toLowerCase(Locale.ROOT);
    Map<Class<? extends Accessor>, Class<? extends Accessor>> accessorMap = accessorProxyMap.get(scopeName);

    if (accessorMap == null) {
      throw new GatewayException("No proxies for scope " + scopeName);
    }

    if (!accessorMap.containsKey(klass)) {
      throw new GatewayException("No proxies for accessor type " + klass.getName());
    }

    return accessorMap.get(klass);
  }
}
